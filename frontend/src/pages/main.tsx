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
import PaymentForm from "../components/User/pay/PaymentForm";
import AddressModal from "../components/User/pay/AddressModal";

export default function Home() {
    const [page, setPage] = useState(0);    // 현재 페이지
    
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
            path: "cart/checkout",
            element: <PaymentForm/>
        },
        {
            path: "detail/:itemId",
            element: <ItemDetail/>
        },
        {
            path: "/popup/addressmanagement",
            element: <AddressModal/>
        },
        {
            path: "test",
            element: <AddressModal/>
        }
    ]);

    return (
        <RouterProvider router={router} />
    )
}