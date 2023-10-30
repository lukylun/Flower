import { useEffect, useState } from "react";
import "./RandomFlower.css"; // 스타일 파일을 불러옵니다.

const BlossomAnimation = ({ onAnimationEnd }) => {
  const [petals, setPetals] = useState([]);

  useEffect(() => {
    const numPetals = 30;
    const newPetals = [];

    for (let i = 0; i < numPetals; i++) {
      const delay = Math.random() * 10; // 각 벚꽃잎의 애니메이션 지연 시간
      const fallDuration = 8 + Math.random() * 5; // 각 벚꽃잎의 낙하 시간

      newPetals.push({ delay, fallDuration });
    }

    setPetals(newPetals);

    // 애니메이션 종료 후에 onAnimationEnd 콜백 호출
    const animationDuration = 8 + Math.random() * 5;
    setTimeout(onAnimationEnd, animationDuration * 1000);
  }, [onAnimationEnd]);

  return (
    <div className="blossom-animation">
      {petals.map((petal, index) => (
        <div
          key={index}
          className="petal"
          style={{
            animationDelay: `${petal.delay}s`,
            animationDuration: `${petal.fallDuration}s`,
          }}
        />
      ))}
    </div>
  );
};

export default BlossomAnimation;