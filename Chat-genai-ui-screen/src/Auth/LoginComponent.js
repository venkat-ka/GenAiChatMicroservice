import { useContext } from "react";
import { getUserLogin } from '../service/AuthService.ts'
import AuthContext, { IAuthRes } from '../ContextData/AuthContext.ts';
import { Navigate, useNavigate } from "react-router-dom";
import useGetUserList from "../service/AuthService.ts";
function LoginComponent() {
    const { getUserList } = useGetUserList();
    const cdd = useContext(AuthContext);
    const { isLoggedIn, getLoggInDetails } = useContext(AuthContext);
    const nav = useNavigate();

    const storeProfileData = (usid) => {
        getUserList('/users-ws/users/' + usid).
            then((res) => {
                console.log("userdetail by user");
                localStorage.setItem('logData', JSON.stringify(res))

            }).catch(err => {
                console.log(err)

            })
    }

    const handleSignIn = () => {
        let reqData = { "email": "admin@test.com", "password": "12345678" }
        const getRes = getUserLogin('/users-ws/users/login', reqData)
            .then((data) => {

                console.log(data.headers['access-token'])
                console.log(data.headers['uid'])
                const getCrediential = { uid: data.headers['uid'], token: data.headers['access-token'], isLoggedIn: true }
                localStorage.setItem('jwt', JSON.stringify(data))

                localStorage.setItem('uid', data.headers['uid'])

                getLoggInDetails(getCrediential)

                storeProfileData(data.headers['uid'])

                setTimeout(() => {
                    nav("/user-list")
                }, 2000)

            }).catch(err => {
                console.log(err)
            })
        console.log(getRes)
    }
    const loginForm = () => {
        return <>
            <div className='userdtls'>
                <div className='fl mb15px'>Email</div><div className='fr mb15px b'><input type="text" name="eml" onChange={(event) => { console.log(event) }} /></div>
                <div className='fl mb15px'>Password</div><div className='fr mb15px b'><input type="password" name="psw" onChange={(event) => { console.log(event) }} /></div>
                <div className="clr"></div>
            </div>
            <div className="button">
                <button type="button" className="btn pd10 mr15px">Clear</button>
                <button type="button" className="btn pd10" onClick={() => handleSignIn()}>Submit</button>
            </div>
        </>
    }
    return loginForm()
}
export default LoginComponent;