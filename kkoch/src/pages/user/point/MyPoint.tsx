import columns from "@/pages/flowerInfo/TableColumns";
import { MDBBtn, MDBCard, MDBCardBody, MDBCardTitle } from "mdb-react-ui-kit";
import MyReservModal from "../MyReservation/MyReservModal";
import StripedDataGrid from "../stripedDataGrid";
import { useEffect, useState } from "react";
import axios from "axios";
import secureLocalStorage from "react-secure-storage";
import MyPointRow from "./MyPointRow";

const MyPoint = () => {

    const [responses, setReponses] = useState([]);

    useEffect(() => {
        axios
            // .get(`/api/api/user-service/${secureLocalStorage.getItem('memberkey')}/points`)
            .get(`https://i9c204.p.ssafy.io/api/user-service/${secureLocalStorage.getItem('memberkey')}/points`)
            .then((response) => {
                const responses = response.data.data.content;
                setReponses(responses);
            })
    }, []);

    const onClickPayment = () => {
        // window.IMP
        // if (!window.IMP) return;
        /* 1. 가맹점 식별하기 */
        // const { IMP } = window;
        // IMP.init("imp70884442"); // 가맹점 식별코드

        /* 2. 결제 데이터 정의하기 */
        const data = {
            pg: "html5_inicis", // PG사 : https://portone.gitbook.io/docs/sdk/javascript-sdk/payrq#undefined-1 참고
            pay_method: "card", // 결제수단
            merchant_uid: `mid_${new Date().getTime()}`, // 주문번호
            amount: 1000, // 결제금액
            name: "아임포트 결제 데이터 분석", // 주문명
            buyer_name: "홍길동", // 구매자 이름
            buyer_tel: "01012341234", // 구매자 전화번호
            buyer_email: "example@example", // 구매자 이메일
            buyer_addr: "신사동 661-16", // 구매자 주소
            buyer_postcode: "06018", // 구매자 우편번호
        };

        /* 4. 결제 창 호출하기 */
        // IMP.request_pay(data, callback);
    }

    const callback = (response) => {
        const {
            success,
            merchant_uid,
            error_msg
        } = response;

        if (success) {
            alert('결제 성공');
        } else {
            alert(`결제 실패: ${error_msg}`);
        }
    }


    return (
        <MDBCard>
            <MDBCardBody>
                <MDBCardTitle className="mb-4 font-bold flex justify-between items-start">
                    <p>나의 포인트 내역</p>
                    <MDBBtn onClick={onClickPayment}>포인트 충전하기</MDBBtn>
                </MDBCardTitle>

                <table className='table-auto w-100'>
                    <colgroup>
                        <col width="10%" />
                        <col width="30%" />
                        <col width="30%" />
                        <col width="30%" />
                    </colgroup>
                    <thead>
                        <tr className='text-center border-bottom h-12'>
                            <th>No</th>
                            <th>금액</th>
                            <th>상태</th>
                            <th>일자</th>
                        </tr>
                    </thead>
                    <tbody>
                        {responses.map(data => (
                            <MyPointRow data={data} key={data.no} />
                        ))}
                    </tbody>
                </table>
            </MDBCardBody>

        </MDBCard>
    )

}

export default MyPoint;