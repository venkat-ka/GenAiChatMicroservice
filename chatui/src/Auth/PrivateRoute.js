import { Navigate, Outlet } from 'react-router-dom'
const PrivateRoute = () => {

    let notLoggedIn = typeof window == 'undefined' || localStorage.getItem('jwt') == null
    console.log(!notLoggedIn)
    let auth = { 'token': !notLoggedIn }
    return (
        auth.token ? <Outlet /> : <Navigate to='/login' />
    )
}
export default PrivateRoute;