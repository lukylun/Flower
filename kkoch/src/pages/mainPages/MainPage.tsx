  // import React, { useState, useEffect } from "react";
  import HomeAuction from "@/pages/mainPages/HomeAuction";
  import HomeFlowers from "@/pages/mainPages/HomeFlowers";
  // import TodayFlower from "@/pages/mainPages/RandomFlower";
  import gif2 from "@/assets/FlowerScatter1.gif";

  function MainPage() {

    return (
      <div className="app bg-gray-20">
        <HomeAuction />
        <img src={gif2} alt="Loading GIF" className="w-[100%] h-[100%] absolute top-[100px] left-0 pointer-events-none"/>
        <HomeFlowers />
      </div>
    );
  }

  export default MainPage;