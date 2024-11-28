import { useContext, useEffect, useState } from 'react'
import AuthContext from '../../ContextData/AuthContext.ts';
import { Navigate, useNavigate } from 'react-router-dom';

const HeaderComponent = ({ isPrf }) => {
    const { isLoggedIn, token, uid, getLoggInDetails } = useContext(AuthContext)
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
        localStorage.removeItem("jwt")
        localStorage.removeItem("uid")
        localStorage.removeItem("logData")
        //s  getLoggInDetails({ uid: null, token: null, isLoggedIn: false })
        setTimeout(() => {
            nav("/login")
        }, 2000)

    }
    // let gh = JSON.parse(localStorage.getItem('logData'));
    //console.log(prNm, 'prNm')
    // console.log(localStorage.getItem('logData'))
    // console.log(gh.data.firstName)
    // console.log()

    return <div className="header pd10"><span className="f-left">AI CHAT</span>

        <span className="f-right" onClick={() => handleClick()}> {!isPrf ? localStorage.getItem('jwt') ? 'Profile' : 'Login' : 'User list'}  </span>
        {prNm && <span className='f-right' onClick={() => handleLogout()}> Logout | </span>}

        <div className="clearBoth"></div></div>
}
export default HeaderComponent