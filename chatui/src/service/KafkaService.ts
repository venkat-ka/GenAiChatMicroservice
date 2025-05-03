import axios from "axios";
import { useContext } from 'react'
import AuthContext from '../ContextData/AuthContext.ts';
const baseUsr: string = 'http://localhost:8082';
function useKafkaApi() {
   const { isLoggedIn } = useContext(AuthContext);
   const callKafkaApi = (endpoint: string, reqData) => {


      //  reqData = {
      //     a: 10,
      //     b: 20,
      //   };
      let getToken = null;
      if (isLoggedIn) {
         getToken = isLoggedIn?.token;
      } else {
         if (localStorage.getItem('jwt') != null) {
            let parserData = JSON.parse(localStorage.getItem('jwt'));

            getToken = parserData.headers['access-token']
         }
      }
      //  let getToken = isLoggedIn ?  :  != null ? localStorage.getItem('jwt') 
      const headerUData = {
         headers: {
            Accept: "application/json",
            "Content-Type": "application/json",
            "Access-Control-Allow-Origin": "*",
            "Access-Control-Allow-Methods": "POST, PUT, PATCH, GET, DELETE, OPTIONS",
            "Access-Control-Allow-Headers": "Origin, X-Api-Key, X-Requested-With, Content-Type, Accept, Authorization, Access-Token, Uid",
            "Access-Control-Expose-Headers": "*",
            "withCredentials": true,
            "Authorization": "Bearer " + getToken
         },
      }
      const headerData = {
         headers: {
            "Authorization": "Bearer " + getToken
         },
      }
      return axios.post(baseUsr + endpoint, reqData, headerUData);

   }
   return { callKafkaApi }
}
export default useKafkaApi;