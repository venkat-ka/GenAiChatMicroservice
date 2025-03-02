import { useContext, useEffect, useState } from 'react'
import AuthContext from '../../ContextData/AuthContext.ts';
import { Navigate, useNavigate } from 'react-router-dom';
import useGetUserList from "../../service/AuthService.ts";

const HeaderComponent = ({ isPrf }) => {
    const { isLoggedIn, token, uid, getLoggInDetails } = useContext(AuthContext)
    const { postUserAuth } = useGetUserList();
    const [logout, setLogout] = useState(false);
    const [prNm, setPrNm] = useState(localStorage.getItem('logData') != undefined ? JSON.parse(localStorage.getItem('logData')) : null);
    const nav = useNavigate()
    useEffect(() => {
        setPrNm(localStorage.getItem('logData') != undefined ? JSON.parse(localStorage.getItem('logData')) : null)
    }, [localStorage.getItem('logData')])
    // useEffect(() => {
    //     if (localStorage.getItem('logData') != null || isLoggedIn != undefined) {
    //         let parserData = JSON.parse(localStorage.getItem('logData'));
    //         console.log(parserData.data['firstName'], 'ggb')

    //         setPrNm(parserData.data['firstName']);
    //         console.log(isLoggedIn)
    //     }
    // }, [])
    const handleClick = () => {
        if (localStorage.getItem('jwt')) {
            if (!isPrf) {
                nav('/')
            } else {
                nav('/user-list')
            }

        }
    }
    const handleLogout = () => {
        if (!logout) {
            setLogout(true);

            postUserAuth('/users-ws/users/signout', { userId: localStorage.getItem('uid') }).
                then((res) => {
                    console.log(res);
                    //setUserList(res.data)
                    setLogout(false);
                }).catch(err => {
                    console.log(err)
                    setLogout(false);
                })
            localStorage.removeItem("jwt")
            localStorage.removeItem("uid")
            localStorage.removeItem("logData")
            //s  getLoggInDetails({ uid: null, token: null, isLoggedIn: false })
            setTimeout(() => {
                nav("/login")
            }, 2000)

        }
    }

    let logOutCss = logout ? 'blur' : ''
    return <div className="header pd10">
        <span className="f-left">AI CHAT</span>
        <span className="cursor f-right" onClick={() => handleClick()}> {!isPrf ? localStorage.getItem('jwt') ? 'Profile' : 'Login' : 'User list'}  </span>
        {prNm && <><span className='cursor f-right' onClick={() => handleLogout()}> Logout | </span>
            <span className={`f-right` + logOutCss}> {'Logged in as'}  <i>   {prNm.data.firstName} </i> | </span></>
        }

        <div className="clearBoth"></div></div>
}
export default HeaderComponent