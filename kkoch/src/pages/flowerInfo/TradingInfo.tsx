import React from 'react'
import TradingTable from './TradingTable'
import { useLocation } from 'react-router'
import TestComponent from './FlowerTable';

const TradingInfo = () => {
  const location = useLocation();
  const codeData = location.state?.code;

  return (
    <div className='gap-24 bg-gray-20 md:h-full md:pb-0'>
      <TradingTable />
      <TestComponent code = {codeData} />
    </div>
  )
}

export default TradingInfo
