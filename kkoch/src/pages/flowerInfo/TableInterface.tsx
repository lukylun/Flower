import dayjs from "dayjs";

interface FilterValues {
  startDate: dayjs.Dayjs;
  endDate: dayjs.Dayjs;
  flower: string;
  variety: string;
  location: string;
  category: string
}

export default FilterValues;