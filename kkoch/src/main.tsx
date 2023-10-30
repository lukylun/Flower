import React from 'react'
import ReactDOM from 'react-dom/client'
import App from './App.tsx'
import './index.css'
import 'mdb-react-ui-kit/dist/css/mdb.min.css';
import "@fortawesome/fontawesome-free/css/all.min.css";
import { Provider } from 'react-redux';
import store from './reducer/store';

ReactDOM.createRoot(document.getElementById('root')!).render(
  <Provider store={store}>
      <App />
      {/* <React.StrictMode>
      </React.StrictMode> */}
  </Provider>
)
