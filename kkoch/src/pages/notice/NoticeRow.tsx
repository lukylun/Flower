import { Link } from 'react-router-dom';

const NoticeRow = ({data}) => {

    return (
        <tr className='border-bottom h-16'>
            <td>{data.no}</td>
            {/* <td className='text-left'>{data.title}</td> */}
            <td className='text-left'><Link to={`/notices/${data.noticeId}`} state={{ no: data.no }}>{data.title}</Link></td>
            <td>관리자</td>
            <td>{data.createdDate}</td>
        </tr>
    )
}

export default NoticeRow;