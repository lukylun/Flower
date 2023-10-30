import { useEffect, useState } from 'react';
import { MDBCard, MDBCardBody, MDBCardTitle } from "mdb-react-ui-kit";
import {GridRowsProp, GridColDef } from '@mui/x-data-grid';
import StripedDataGrid from "../stripedDataGrid";
import axios from 'axios';
import secureLocalStorage from 'react-secure-storage';


function MyAuctionList() {
  const initialRows: GridRowsProp = [];

  const columns: GridColDef[] = [
    { field: 'col1', headerName: 'No', width: 110 },
    { field: 'col2', headerName: '품목', width: 110 },
    { field: 'col3', headerName: '품종', width: 110 },
    { field: 'col4', headerName: '등급', width: 110 },
    { field: 'col5', headerName: '지역', width: 110 },
    { field: 'col6', headerName: '단수', width: 110 },
    { field: 'col7', headerName: '가격', width: 110 },
    { field: 'col8', headerName: '낙찰 시간', width: 160, align: 'center', headerAlign: 'center' },
    { field: 'col9', headerName: '픽업 현황', width: 110 },
    // {
    //   field: 'col10',
    //   headerName: '낙찰 내역 삭제',
    //   width: 150,
    //   renderCell: (params) => (
    //     <button className='delete-btn' onClick={() => handleDelete(params.row.id)}>삭제하기</button>
    //   ),
    // },
  ];

  const [rows, setRows] = useState<GridRowsProp>(initialRows);
  
  
  // 회원 낙찰 내역 가져오기
  useEffect(() => {
    axios({
      method: "get",
      url: `https://i9c204.p.ssafy.io/api/admin-service/trades/${secureLocalStorage.getItem("memberkey")}?term=1&page=0`,
      // url: `/api/api/admin-service/trades/${secureLocalStorage.getItem("memberkey")}?term=1&page=0`,
      headers: {
        Authorization: `Bearer ${secureLocalStorage.getItem("token")}`
      }
    })
    .then(res => {
      console.log(res);
      const rawData = res.data.data;

      const formattedData = rawData["content"].map((item, idx) => ({
        id: idx + 1,
        col1: idx + 1,
        col2: item.type,
        col3: item.name,
        col4: item.grade,
        col5: item.region,
        col6: item.count,
        col7: item.bidPrice,
        col8: item.bidTime,
        col9: "픽업 대기",
      }));

      setRows(formattedData);
    })
    .catch(err => {
      console.log(err);
    })
  }, []);

  // const pk = 1
  // const handleDelete = (id) => {
  //   // 클릭한 행을 삭제하는 로직 구현
  //   axios({
  //     method: "delete",
  //     url: `/api/admin-service/trades/${pk}/${id}`
  //   })
  //   .then((res) => {
  //     console.log(res);
  //     // 서버에서 삭제 성공하면 rows 상태 업데이트하여 화면에서도 삭제
  //     // const updatedRows = rows.filter((row) => row.id !== id);
  //     // setRows(updatedRows);
  //   })
  //   .catch(err => {
  //     console.log(err);
  //   });
  // };
  

  
  return (
    <MDBCard>
      <MDBCardBody>
        <MDBCardTitle className="mb-4 font-bold">
          나의 낙찰 내역
        </MDBCardTitle>

        <div className="h-full w-full">
          <StripedDataGrid
            rows={rows} columns={columns}
            getRowClassName={(params) =>
              params.indexRelativeToCurrentPage % 2 === 0 ? 'even' : 'odd'
            }
            pageSizeOptions={[5, 10, 25]}
          />
        </div>

      </MDBCardBody>
    </MDBCard>
  )
}

export default MyAuctionList