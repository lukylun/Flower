import { createSlice, PayloadAction } from '@reduxjs/toolkit';
import secureLocalStorage  from  "react-secure-storage";

interface AuthState {
  memberkey: string | null;
  token: string | null;
  email: string | null;
  username: string | null;
}

const initialState: AuthState = {
  memberkey: null,
  token: null,
  email: null,
  username: null,
};

const authSlice = createSlice({
  name: 'auth',
  initialState,
  reducers: {
    login: (state, action: PayloadAction<object>) => {
      state.memberkey = action.payload["mem_key"];
      state.token = action.payload["mem_token"];

      secureLocalStorage.setItem("memberkey", state.memberkey);
      secureLocalStorage.setItem("token", state.token);
      secureLocalStorage.setItem("loginTime", new Date());
      
    },
    logout: (state) => {
      state.memberkey = null;
      state.token = null;
      secureLocalStorage.removeItem("memberkey");
      secureLocalStorage.removeItem("token");
      secureLocalStorage.removeItem("loginTime");
    },

    setEmail: (state, action) => {
      state.email = action.payload;
    },

    setUsername: (state, action) => {
      state.username = action.payload;
    }
  },
});


export const { login, logout, setEmail, setUsername } = authSlice.actions;
export default authSlice.reducer;
