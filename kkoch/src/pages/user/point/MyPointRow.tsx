const MyPointRow = ({data}) => {

    return (
        <tr className='text-center border-bottom h-12'>
            <td>{data.no}</td>
            <td className='text-rigth'>{data.amount}</td>
            <td>{data.status}</td>
            <td>{data.createdDate}</td>
        </tr>
    );
}

export default MyPointRow;