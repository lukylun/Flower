import Navbar from "@/pages/navbar";
import {BrowserRouter, Route, Routes} from "react-router-dom";
import { useEffect, useState } from "react";
import AuctionLocation from "@/pages/auction/AuctionLocation";
import AuctionLiveSession from "@/pages/auction/AuctoinLiveSession";
import MainPage from "@/pages/mainPages/MainPage";
import Form from "@/pages/user/form/Form";
import MyPage from "@/pages/user/profile/MyPage";
import SelectArea from "@/pages/admin/SelectArea";
import OpenSession from "@/pages/admin/OpenSession";
import TradingInfo from "@/pages/flowerInfo/TradingInfo"
import FlowerDetail from "@/pages/flowerInfo/FlowerDetail";
import Notice from "@/pages/notice/Notice";
import NoticeDetail from "@/pages/notice/NoticeDetail";
import NotFound404 from "@/pages/NotFound404/NotFound404";
import secureLocalStorage from "react-secure-storage";
import { useDispatch } from "react-redux";
import { logout } from "./reducer/store/authSlice";


function App() {
  const [isLoggedIn, setIsLoggedIn] = useState<boolean>(false);
  const dispatch = useDispatch();

  useEffect(() => {
    
    const checkLoginStatus = () => {
      const loginTime = secureLocalStorage.getItem('loginTime');
      
      const nowDate = new Date();
      const logTime = new Date(String(loginTime));
      const diffMSec = nowDate.getTime() - logTime.getTime();
      const diffHour = diffMSec / (60 * 60 * 1000);
      console.log(diffMSec, diffHour)
      // 3시간 자동 로그인 시간이 지나면 자동 로그아웃
      if (loginTime && diffHour < 5) {
        setIsLoggedIn(true);
      } else {
        setIsLoggedIn(false);
        dispatch(logout());
      }
    };
    
    checkLoginStatus();
  }, []);

  const [isTop, setIsTopOfPage] = useState<boolean>(true);
  const showNavbar = ["/login", "/signup"].includes(location.pathname) ? false : true;
  console.log(showNavbar);

  useEffect(() => {

    const handleScroll = () => {
      if (window.scrollY === 0) {
        setIsTopOfPage(true);
      }
      if (window.scrollY !== 0) setIsTopOfPage(false);
    };

    window.addEventListener("scroll", handleScroll);
    return () => window.removeEventListener("scroll", handleScroll);
  }, []);
  
  return (
    <div className="app">
      <BrowserRouter>
        <Navbar isTop={isTop}/>
        {/* 라우팅 정보 */}
        <Routes>
          <Route path={"/"} element={<MainPage />}/>
          <Route path={"/auction"} element={<AuctionLocation/>} />
          <Route path={"/auction/liveSession"} element={<AuctionLiveSession/>}/>

          {/* <Route path={"/auction/liveroom"} element={<AuctionLiveRoom/>}/> */}

          <Route path={"/flowers/"} element={<TradingInfo />}/>
          <Route path={"/flowers/:code"} element={<TradingInfo />}/>
          <Route path={"/flowers/info/:type/:name"} element={<FlowerDetail />}/>
          
          <Route path={"/notices"} element={<Notice />}/>
          <Route path={"/notices/:noticeId"} element={<NoticeDetail />}/>
 
          {/* 로그인 */}
          <Route path={"/login"} element={<Form props={true}/>}/>
          <Route path={"/signup"} element={<Form props={false}/>}/>

          {/* 마이페이지 */}
          <Route path={"/mypage"} element={<MyPage/>}/>

          {/* 관리자 경매방 생성 경로 */}
          <Route path={"/admin/selectArea"} element={<SelectArea/>} />
          <Route path={"/admin/openSession"} element={<OpenSession/>} />

          {/* 나머지 경로는 404 페이지로 */}
          <Route path={"*"}  element={<NotFound404 />}  />
        </Routes>
        
      </BrowserRouter>
    </div>
  )
}

export default App
