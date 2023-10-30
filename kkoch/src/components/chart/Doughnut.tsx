import { Chart as ChartJS, ArcElement, Tooltip, Legend } from 'chart.js';
import { useEffect, useState } from 'react';
import { Doughnut } from 'react-chartjs-2';

ChartJS.register(ArcElement, Tooltip, Legend);

export function DoughnutChart({ isBiddingActive, key }) {
  // console.log("그래프활성화확인", isBiddingActive)
  const initialGraph = 40
  const [graph, setGraph] = useState(initialGraph); // 초기 그래프 크기 설정
  const [isGraphCreated, setIsGraphCreated] = useState(false);

  useEffect(() => {
    if (isGraphCreated) {
      setGraph(initialGraph); // 새로운 경매 정보로 업데이트될 때 그래프 초기화
    } else {
      setIsGraphCreated(true);
    }
  }, [key, isGraphCreated]);

  const calculateColor = (redValue) => {
    // 데이터가 25일 때까지는 파란색
    if (redValue >= 25) {
      const hue = 240;
      return `hsl(${hue}, 100%, 50%)`;
    // 데이터가 10일때까지는 노란색
    } else if (redValue >= 10) {
      const hue = 60;
      return `hsl(${hue}, 100%, 50%)`;

    // 그 이하는 빨간색
    } else {
      return `hsl(0, 100%, 50%)`;
    }
  };

  // useEffect(() => {
  //   setIsGraphCreated(true);
  // }, []);


  const data = {
    datasets: [
      {
        data: [graph, 100-graph],
        backgroundColor: [
          calculateColor(graph),
          'rgba(0, 0, 0, 0)'
        ],
        borderColor: [
          calculateColor(graph),
          'rgba(0, 0, 0, 0)'
        ],
        borderWidth: 0.5,
      },
    ],
  };

  const options = {
    rotation: 230, // 도넛 차트를 회전
    responsive: false
  };

  let animationStartTime;
  const animationDuration = 10000; // 10초
  const decreaseAmountPerMs = 0.009; // 1초당 감소하는 양

  const decreaseData = (timestamp) => {
    if (!isBiddingActive) {
      return ;
    }
    if (!animationStartTime) {
      animationStartTime = timestamp;
    }

    const elapsedTime = timestamp - animationStartTime;
    const progress = Math.min(elapsedTime / animationDuration, 1);
    const updatedGraph = Math.max(graph - progress * decreaseAmountPerMs * animationDuration, 0);
    setGraph(updatedGraph);
  };

  const animate = (timestamp) => {
    if (!isBiddingActive) {
      return;
    }
    decreaseData(timestamp);

    if (graph > 0) {
      requestAnimationFrame(animate);
    }
  };

  useEffect(() => {
    if (isBiddingActive) {
      requestAnimationFrame(animate);
    } else {
      animationStartTime = undefined;
    }

    return () => {
      animationStartTime = undefined; // 애니메이션 시작 시간 초기화
    };
  }, [graph, isBiddingActive, isGraphCreated]);

  return <Doughnut key={key} data={data} options={options} />;
}
