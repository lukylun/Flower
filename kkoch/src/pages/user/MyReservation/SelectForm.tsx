import axios from 'axios';
import { useState } from 'react';
import Select, { components, StylesConfig } from 'react-select'
import AsyncSelect from 'react-select/async'

const customStyles: StylesConfig = {
  control: (base) => ({
    ...base,
    flexDirection: 'row-reverse',
  }),
  clearIndicator: (base) => ({
    ...base,
    position: 'absolute',
    right: 0,
  }),
}

export default function SelectForm({
    onFlowerChange,
    onVarietyChange
  }) 
  {
  // 부류, 품목, 품종 상태를 useState로 관리
  const [categoryOptions, setCategoryOptions] = useState([
    { value: '절화', label: '절화' },
    { value: '관엽', label: '관엽' },
    { value: '난', label: '난' },
    { value: '춘란', label: '춘란' },
  ]);
  const [flowerOptions, setFlowerOptions] = useState([]);
  const [varietyOptions, setVarietyOptions] = useState([]);

  // 부류 선택
  const [category, setCategory] = useState<string>('')
  // 품목 선택
  const [flower, setFlower] = useState<string>('')
  // 품종 선택
  const [variety, setVariety] = useState<string>('')

  // 부류가 선택되면 품목을 가져온다
  const callFlowers = (e) => {
    // 초기화
    setFlower('');
    setVariety('');
    setFlowerOptions([]);
    setVarietyOptions([]);
    e && axios({
      method: "get",
      url: `https://i9c204.p.ssafy.io/api/admin-service/categories/type?code=${e}`
      // url: `/api/api/admin-service/categories/type?code=${e}`
    })
    .then(res => {
      // console.log(res.data.data);
      const tmp = res.data.data.map((e) => {
        return {
          value: e,
          label: e
        }
      })
      console.log(tmp);
      setFlowerOptions(tmp);
      setCategory(e);
    })
    .catch(err => {
      console.log(err);
    })
  };

  // 품목이 선택되면 품종을 가져온다
  const callVarieties = (e) => {
    // 초기화
    setVariety('');
    setVarietyOptions([]);
    e && axios({
      method: "get",
      url: `https://i9c204.p.ssafy.io/api/admin-service/categories/name?code=${category}&type=${e}`
      // url: `/api/api/admin-service/categories/name?code=${category}&type=${e}`
    })
    .then(res => {
      // console.log(res.data.data);
      const tmp = res.data.data.map((e) => {
        return {
          value: e,
          label: e
        }
      })
      console.log(tmp);
      setVarietyOptions(tmp);
      setFlower(e);
    })
    .catch(err => {
      console.log(err);
    })
  }

  const filterFlower = (inputValue: string) => {
    return flowerOptions.filter((i) =>
      i.label.toLowerCase().includes(inputValue.toLowerCase())
    );
  };

  const filterVariety = (inputValue: string) => {
    return varietyOptions.filter((i) =>
      i.label.toLowerCase().includes(inputValue.toLowerCase())
    );
  };
  
  const loadFlowerOptions = (inputValue, callback) => {
    setTimeout(() => {
        callback(filterFlower(inputValue));
    }, 100);
  };

  const loadVarietyOptions = (inputValue, callback) => {
    setTimeout(() => {
        callback(filterVariety(inputValue));
    }, 100);
  };

  const handleSelectFlower = (selectedOption) => {
    setFlower(selectedOption ? selectedOption["value"] : '');
    callVarieties(selectedOption ? selectedOption["value"] : '');
    // 부모 컴포넌트로 값 전송
    onFlowerChange(selectedOption ? selectedOption["value"] : '');
  }

  const handleSelectVariety = (selectedOption) => {
    setVariety(selectedOption ? selectedOption["value"] : '');
    // 부모 컴포넌트로 값 전송
    onVarietyChange(selectedOption ? selectedOption["value"] : '');
  }

  return (
    <>
      <Select
        placeholder="부류"
        options={categoryOptions} 
        isClearable={true}
        components={{
          IndicatorSeparator: () => null,
          DropdownIndicator,
        }}
        styles={customStyles}
        filterOption={null}
        onChange={(selectedOption) => {
          setCategory(selectedOption ? selectedOption["value"] : '')
          callFlowers(selectedOption ? selectedOption["value"] : '')
        }} // 셀렉트 변경 핸들러 등록
      />

      <div className='mt-3 flex justify-between'>
        <AsyncSelect 
          className='w-[48%]'
          placeholder="품목"
          defaultOptions={flowerOptions} 
          isClearable={true}
          loadOptions={loadFlowerOptions}
          onChange={handleSelectFlower}
        />

        <AsyncSelect 
          className='w-[48%]'
          placeholder="품종"
          defaultOptions={varietyOptions} 
          isClearable={true} 
          loadOptions={loadVarietyOptions}
          onChange={handleSelectVariety}
        />
      </div>
    </>
  );
}

const DropdownIndicator = (props) => {
  return (
    components.DropdownIndicator && (
      <components.DropdownIndicator {...props}>
        <SearchIcon />
      </components.DropdownIndicator>
    )
  )
}

const SearchIcon = () => (
  <svg
    width="22"
    height="22"
    viewBox="0 0 100 100"
    fill="none"
    xmlns="http://www.w3.org/2000/svg"
  >
    <circle cx="38" cy="40" r="20.5" stroke="currentColor" strokeWidth="7" />
    <path
      d="M76.0872 84.4699C78.056 86.4061 81.2217 86.3797 83.158 84.4109C85.0943 82.442 85.0679 79.2763 83.099 77.34L76.0872 84.4699ZM50.4199 59.2273L76.0872 84.4699L83.099 77.34L57.4317 52.0974L50.4199 59.2273Z"
      fill="currentColor"
    />
  </svg>
)