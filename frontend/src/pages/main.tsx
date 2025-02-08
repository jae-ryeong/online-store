import { createBrowserRouter, RouterProvider } from "react-router-dom";
import MainComponent from "../components/MainComponent";
import Login from "../components/User/login/login";
import { createContext, useEffect, useState } from "react";
import UserTypeSelectForm from "../components/User/Join/JoinSelect";
import CustomerJoinForm from "../components/User/Join/CustomerJoin";
import SellerJoin from "../components/User/Join/SellerJoin";
import MyPage from "../components/User/myPage/myPage";
import CartCard from "../components/User/myPage/cart/CartCard";
import OrderList from "../components/User/myPage/orderList";
import ItemManagement from "../components/User/myPage/seller/itemManagement";
import ItemForm from "../components/User/myPage/seller/itemForm";

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
                {path: "cart", element: <CartCard/>},
                {path: "seller/itemManagement", element: <ItemManagement/>},
                {path: "seller/itemRegistration", element:<ItemForm/>}
            ]
        },
        
    ]);

    return (
        <RouterProvider router={router} />
    )
}