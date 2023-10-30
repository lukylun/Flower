import axios from 'axios';
import NoticeRow from './NoticeRow';
import { useEffect, useState } from 'react';

const Notice = () => {

    let [responses, setResponses] = useState([]);

    useEffect(() => {
        axios
            .get(`https://i9c204.p.ssafy.io/api/admin-service/notices`)
            // .get(`/api/api/admin-service/notices?type=1&keyword=&pageNum=1`)
            .then((response) => {
                responses = response.data.data.content;
                setResponses(responses);
                // console.log(responses);
            })
        
    } , []);

    return (
        <div className='gap-24 bg-gray-20 md:h-full md:pb-0'>
            <h1 className='text-center mt-5 mb-5'>공지사항</h1>
            <div className='container  items-center justify-self-center'>
                <table className='table-auto w-100 border-collapse border-slate-500'>
                    <thead className='text-center text-xl border-bottom border-top'>
                        <tr className='h-16'>
                            <th>번호</th>
                            <th>제목</th>
                            <th>작성자</th>
                            <th>작성일</th>
                        </tr>
                    </thead>
                    <tbody className='text-center'>
                        {responses.map(data => (
                            <NoticeRow data={data} key={data.noticeId}/>
                        ))}
                    </tbody>
                </table>
            </div>
        </div>
    )
}

export default Notice;