import { useContext, useState } from "react";
import { getUserLogin } from '../service/AuthService.ts'
import AuthContext, { IAuthRes } from '../ContextData/AuthContext.ts';
import { Navigate, useNavigate } from "react-router-dom";
import useGetUserList from "../service/AuthService.ts";
function LoginComponent() {
    const { getUserList } = useGetUserList();
    const cdd = useContext(AuthContext);
    const { isLoggedIn, getLoggInDetails } = useContext(AuthContext);
    const [email, setEmail] = useState();
    const [password, setPassword] = useState();
    const [isLoading, setIsLoading] = useState();
    const nav = useNavigate();

    const storeProfileData = (usid) => {
        setIsLoading(true)
        getUserList('/users-ws/users/' + usid).
            then((res) => {
                setIsLoading(false)
                console.log("userdetail by user");
                localStorage.setItem('logData', JSON.stringify(res))

            }).catch(err => {
                setIsLoading(false)
                console.log(err)
                alert(err)
            })
    }

    const handleSignIn = () => {
        setIsLoading(true)
        let reqData = { "email": email, "password": password }
        const getRes = getUserLogin('/users-ws/users/login', reqData)
            .then((data) => {

                console.log(data.headers['access-token'])
                console.log(data.headers['uid'])
                const getCrediential = { uid: data.headers['uid'], token: data.headers['access-token'], isLoggedIn: true }
                localStorage.setItem('jwt', JSON.stringify(data))

                localStorage.setItem('uid', data.headers['uid'])

                getLoggInDetails(getCrediential)

                storeProfileData(data.headers['uid'])
                setIsLoading(false)
                setTimeout(() => {
                    nav("/user-list")
                }, 2000)

            }).catch(err => {
                setIsLoading(false)
                console.log(err)
                alert(err)
            })
        console.log(getRes)
    }
    const loginForm = () => {
        return <>
            <div className='userdtls' style={{ 'opacity': isLoading ? '0.5' : '1' }}>

                <div className='fl mb15px'>Email</div><div className='fr mb15px b'><input type="text" name="eml" onChange={(event) => { setEmail(event.target.value) }} /></div>
                <div className='fl mb15px'>Password</div><div className='fr mb15px b'><input type="password" name="psw" onChange={(event) => { setPassword(event.target.value) }} /></div>
                <div className="clr"></div>
            </div>
            <div className="button">
                <button type="button" className="cursor btn pd10 mr15px">Clear</button>
                <button type="button" className="cursor btn pd10" onClick={() => handleSignIn()}>Submit</button>
            </div>
            {isLoading && (<div className="loader"></div>)}
        </>
    }
    return loginForm()
}
export default LoginComponent;