// import React from 'react';
// import ReactDOM from 'react-dom/client';
// import App from './App';



import React from 'react';
import ReactDOM from 'react-dom/client';
import './index.css';
import App from './App.js';
import reportWebVitals from './reportWebVitals.js';
//import { Router, Routes, Route } from 'react-router-dom';
import PrivateRoute from './Auth/PrivateRoute.js';

import { BrowserRouter as Router, Routes, Route } from 'react-router-dom'
import {
    createBrowserRouter,
    RouterProvider,
    createRoutesFromElements
} from "react-router-dom";
import UserManagment from './Component/UserManagment.jsx';
import ProfileComponents from './Component/ProfileComponents.jsx';
import UserList from './Component/UserList.jsx';
import ReadUserMessage from './Component/ReadUserMessage.jsx';
const Root = ({ child }) => { console.log("cccc check"); return <div>homec {child}</div> }
const Dashboard = () => {
    return <div> mmvnn private </div>
}
const Chil = () => { console.log("cccc ch"); return <div>schil</div> }

const RouterEntry = () => {
    return <Router>
        <Routes>
            <Route element={<PrivateRoute />}>
                <Route path='/' element={<ProfileComponents />} />
                <Route path='/user-list' element={<UserList />} />
                <Route exact path="/user-list/:id" element={<ReadUserMessage />} />
            </Route>
            <Route path='/login' element={<UserManagment />} />
        </Routes>
    </Router>
}
const Team = () => {
    return <div>teamm</div>
}

export default RouterEntry