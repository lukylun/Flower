export interface FlowerData {
  bidPrice: number;
  bidTime: string;
  code: string;
  count: number;
  grade: string;
  id: number;
  name: string;
  region: string;
  type: string; // 예시 타입. 실제 데이터에 따라 수정해야 함
}

export interface flowerSeries {
  data: Array<[number, number]>; // 배열의 각 항목은 순서쌍 [timestamp, value]를 나타냄
  color: string;
}