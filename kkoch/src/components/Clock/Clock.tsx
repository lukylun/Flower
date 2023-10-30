import { useState, useEffect } from 'react';

const Clock = () => {
  const days = ['일', '월', '화', '수', '목', '금', '토'];
  const months = ['1', '2', '3', '4', '5', '6', '7', '8', '9', '10', '11', '12'];

  const [date, setDate] = useState(new Date());

  const pad = (num, digit) => {
    let zero = '';
    for (let i = 0; i < digit; ++i) zero += '0';
    return (zero + num).slice(-digit);
  };

  const tick = () => {
    setDate(new Date());
  };

  useEffect(() => {
    const timer = setInterval(tick, 1000);
    return () => clearInterval(timer);
  }, []);

  const h = pad(date.getHours(), 2);
  const m = pad(date.getMinutes(), 2);
  const s = pad(date.getSeconds(), 2);
  const d = `${months[date.getMonth()]}월 ${date.getDate()}일 (${days[date.getDay()]}), ${date.getFullYear()}`;

  return (
    <div className='mt-[50px]'>
      <h2 className='text-center'>현재 시간</h2>
      <p className="text-[2vw] text-center">{d}</p>
      <hr className='w-[70%] mx-auto'/>
      <div className='flex justify-center'>
        <h3 className="inline-block text-[5vw] font-light">{h}&nbsp;:</h3>
        <h3 className="inline-block text-[5vw] font-light">&nbsp;{m}&nbsp;:</h3>
        <h3 className="inline-block text-[5vw] font-light">&nbsp;{s}</h3>
      </div>
    </div>
  );
};

export default Clock;
