import axios from 'axios';
import React, { useEffect, useState } from 'react'
import { useParams, useLocation } from 'react-router-dom';

const NoticeDetail = () => {

    const [notice, setNotice] = useState({});
    const { noticeId } = useParams();
    const location = useLocation();
	const no = location.state?.no;

    useEffect(() => {
        axios
            .get(`https://i9c204.p.ssafy.io/api/admin-service/notices/${noticeId}`)
            // .get(`/api/api/admin-service/notices/${noticeId}`)
            .then((response) => {       
                console.log(response.data.data);
                setNotice(response.data.data);
            })
    }, []);
  return (
    <div className='gap-24 bg-gray-20 md:h-full md:pb-0'>
        <h1 className='text-center mt-5 mb-5'>공지사항</h1>
        <div className='container  items-center justify-self-center'>
                <table className='table-auto w-100 border-collapse border-slate-500'>
                    <colgroup>
                    <col width="25%"/>
                    <col width="25%"/>
                    <col width="25%"/>
                    <col width="25%"/>
                    </colgroup>
                    <tbody className='text-center'>
                        <tr className='border-bottom border-top h-16'>
                            <th className='border-end'>번호</th>
                            <td className='border-end text-left'>{ no }</td>
                            <th className='border-end'>작성자</th>
                            <td className='text-left'>관리자</td>
                        </tr>
                        <tr className='border-bottom h-16'>
                            <th className='border-end'>제목</th>
                            <td colSpan={3} className='text-left'>{ notice["title"] }</td>
                        </tr>
                        <tr className='border-bottom h-16'>
                            <th className='border-end'>내용</th>
                            <td colSpan={3} className='text-left' dangerouslySetInnerHTML={{__html:notice["content"]}}></td>
                            {/* <td colSpan={3} className='text-left'>{ notice["content"] }</td> */}
                        </tr>
                    </tbody>
                </table>
            </div>
    </div>
  )
}

export default NoticeDetail;