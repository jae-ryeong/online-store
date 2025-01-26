import { createBrowserRouter, RouterProvider } from "react-router-dom";
import MainComponent from "../components/MainComponent";
import Login from "../components/User/login/login";
import { createContext, useEffect, useState } from "react";
import UserTypeSelectForm from "../components/User/Join/JoinSelect";
import CustomerJoinForm from "../components/User/Join/CustomerJoin";
import DashboardLayout from "../components/test/dashboardlayout";
import Profile from "../components/test/profile";
import Settings from "../components/test/Settings";
import Tts from "../components/test/tts";
import SellerJoin from "../components/User/Join/SellerJoin";
import MyPage from "../components/User/myPage/myPage";
import CartCard from "../components/User/myPage/cart/CartCard";
import OrderList from "../components/User/myPage/orderList";

interface AuthContextType {
    isLogIn: boolean;
    accessToken: string | null;
    setAccessToken: (token: string | null) => void;
}

const AuthContext = createContext<AuthContextType>({
    isLogIn: false,
    accessToken: null,
    setAccessToken: () => { }
});

export default function Home() {
    const [accessToken, setAccessToken] = useState<string | null>(null);

    useEffect(() => {
        //localStorage에서 token을 가져와 로그인 상태 설정
        const token = localStorage.getItem("accessToken");
        if (token) {
            setAccessToken(token);
        }
    }, [])

    const router = createBrowserRouter([
        {
            path: "/",
            element: <MainComponent />
        },
        {
            path: "login",
            element: <Login />
        },
        {
            path: "join",
            element: <UserTypeSelectForm />
        },
        {
            path: "join/customerjoin",
            element: <CustomerJoinForm />
        },
        {
            path: "join/sellerjoin",
            element: <SellerJoin />
        },
        {
            path: "mypage",
            element: <MyPage />,
            children: [
                {path: "orderlist", element: <OrderList/>},
                {path: "cart", element: <CartCard/>}
            ]
        },
        {
            path: "test",
            element: <Tts />,
            children: [
                {
                    path: "dashboard",
                    element: <DashboardLayout />, // 대시보드 레이아웃
                    children: [
                        { path: "profile", element: <Profile /> }, // 프로필 페이지
                        { path: "settings", element: <Settings /> }, // 설정 페이지
                    ],
                },
            ],
        }
    ]);

    return (
        <RouterProvider router={router} />
    )
}