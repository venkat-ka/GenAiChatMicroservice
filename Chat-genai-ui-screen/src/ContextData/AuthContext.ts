import { createContext } from "react";


const AuthContext = createContext({ uid: null, token: null, isLoggedIn: false });
export default AuthContext;