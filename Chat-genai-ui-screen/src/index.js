import React from 'react';
import ReactDOM from 'react-dom/client';
import './index.css';
import App from './App';
import WebSock from './WebSock.js';
//import SampleComponent from './ReactStom.js';
import reportWebVitals from './reportWebVitals';
//import RxRen from './RxStomp.tsx'
import WebS from './Component/WebS.js'
const root = ReactDOM.createRoot(document.getElementById('root'));
/*
react-stomp
sockjs-client
sockjs
*/
root.render(
  <div>
    <App />
    {/* <WebSock /> */}
    {/* <WebS /> */}
    {/* <SampleComponent /> */}
    {/* <RxRen /> */}
  </div>
);

// If you want to start measuring performance in your app, pass a function
// to log results (for example: reportWebVitals(console.log))
// or send to an analytics endpoint. Learn more: https://bit.ly/CRA-vitals
reportWebVitals();
