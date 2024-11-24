import { createBrowserRouter, RouterProvider } from "react-router-dom";
import MainComponent from "../components/MainComponent";
import Login from "../components/User/login";
import { createContext, useEffect, useState } from "react";
import App from "../test";
import axios from "axios";

interface AuthContextType {
    isLogIn: boolean;
    accessToken: string|null;
    setAccessToken: (token: string | null) => void;
}

const AuthContext = createContext<AuthContextType>({
    isLogIn: false,
    accessToken:null,
    setAccessToken: () => {}
});

export default function Home() {
    const [accessToken, setAccessToken] = useState<string | null>(null);

    useEffect(() => {
        //localStorage에서 token을 가져와 로그인 상태 설정
        const token = localStorage.getItem("accessToken");
        if(token){
            setAccessToken(token);
        }
    },[])

    const router = createBrowserRouter([
        {
            path: "/",
            element: <MainComponent/>
        },
        {
            path: "login",
            element: <Login/>
        },
        {
            path:"join",
            element: <App/>
        }
    ]);

    return(
        <RouterProvider router={router}/>
    )
}