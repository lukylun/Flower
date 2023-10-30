import { combineReducers, configureStore } from '@reduxjs/toolkit';
import authReducer from './authSlice';

const rootReducer = combineReducers({
  // 여기에 각 리듀서를 추가하세요 (ex: authReducer, ... )
  auth: authReducer,
});

const store = configureStore({
  reducer : rootReducer
})


export default store 
export type RootState = ReturnType<typeof store.getState>;