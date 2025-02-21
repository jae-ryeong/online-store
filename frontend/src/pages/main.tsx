import { createBrowserRouter, RouterProvider } from "react-router-dom";
import MainComponent from "../components/MainComponent";
import Login from "../components/User/login/login";
import { useEffect, useState } from "react";
import UserTypeSelectForm from "../components/User/Join/JoinSelect";
import CustomerJoinForm from "../components/User/Join/CustomerJoin";
import SellerJoin from "../components/User/Join/SellerJoin";
import MyPage from "../components/User/myPage/myPage";
import CartCard from "../components/User/myPage/cart/CartCard";
import OrderList from "../components/User/myPage/orderList";
import ItemManagement from "../components/User/myPage/seller/itemManagement";
import ItemForm from "../components/User/myPage/seller/itemForm";
import Contents from "../components/content/contents";
import ItemDetail from "../components/item/detail/ItemDetail";
import axios from "axios";

export default function Home() {
    const [accessToken, setAccessToken] = useState<string | null>(null);
    const [page, setPage] = useState(0);    // 현재 페이지
    const [isAuth, setIsAuth] = useState<boolean>(false);

    useEffect(() => {
        const checkAuth = async() => {
            const token = localStorage.getItem("accessToken");

            if(!token){
                console.log("토큰이 없습니다.")
                setIsAuth(false);
                return;
            }

            try{
                const response = await axios.post("http://localhost:8080/api/v1/user/auth/check",{},
                {
                    headers: {
                        Authorization: `Bearer ${token}`
                    },
                });
                setIsAuth(response.data.valid);
            }catch(error){
                console.error("JWT 검증 실패: ", error);
                localStorage.removeItem("accessToken");
                window.location.reload();
                setIsAuth(false);
            }
        }
        checkAuth();
    },[])

    const router = createBrowserRouter([
        {
            path: "category",
            element: <MainComponent setPage={setPage}/>,
            children: [
                {path: "home", element: <Contents page={page} setPage={setPage}/>},
                {path: "book", element: <Contents page={page} setPage={setPage}/>},
                {path: "food", element: <Contents page={page} setPage={setPage}/>},
                {path: "clothes", element:<Contents page={page} setPage={setPage}/>},
                {path: "pet", element:<Contents page={page} setPage={setPage}/>}
            ]
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
        {
            path: "detail/:itemId",
            element: <ItemDetail/>
        }
        
    ]);

    return (
        <RouterProvider router={router} />
    )
}