import { useEffect, useState } from 'react';
import UserDetailComponent from './UserDetailComponent.jsx';
import LoginComponent from '../Auth/LoginComponent.js';
import HeaderComponent from './Header/HeaderComponents.tsx';
import AuthContext from '../ContextData/AuthContext.ts';

import { Navigate, useNavigate } from 'react-router-dom';
function UserManagment() {
    const nav = useNavigate();
    const [isLoggedIn, setIsloggedIn] = useState();
    const [tab, setTab] = useState(0);

    const getLoggInDetails = (credDtls) => {

        setIsloggedIn({ uid: credDtls.uid, token: credDtls.token, isLoggedIn: credDtls.token != null })
    }

    return <>
        <AuthContext.Provider value={{ isLoggedIn, getLoggInDetails }} >
            <div className='container'>
                <div className="page">
                    <HeaderComponent />
                    <div className='tabs'>
                        <div className='tab-box'>
                            <div className={"cursor chat-tab mr15px f-left pd10 " + (tab == 0 ? 'active' : '')} onClick={() => setTab(0)}>Login</div>
                            <div className={"cursor chat-tab f-right pd10 " + (tab == 1 ? 'active' : '')} onClick={() => setTab(1)}>Create Account
                            </div>
                            <div className='clearBoth'></div>
                        </div>
                    </div>
                    <div className="chatBox">

                        <div className="login-box pd10">
                            {(!isLoggedIn?.isLoggedIn || !isLoggedIn?.isLoggedIn) && tab == 0 && <LoginComponent selectTab={(v) => setTab(v)} />}
                        </div>
                        <div className='create-account-box'>
                            {(!isLoggedIn?.isLoggedIn || !isLoggedIn?.isLoggedIn) && tab == 1 && <UserDetailComponent selectTab={(v) => setTab(v)} />}
                        </div>

                    </div>
                </div>
            </div>
        </AuthContext.Provider > </>


}
export default UserManagment;