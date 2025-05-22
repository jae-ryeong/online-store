import styled from "styled-components";
import Header from "../../header/TotalHeader";
import "../../../fonts/font.css"
import { Outlet, useNavigate } from "react-router-dom";
import axios from "axios";
import { useEffect, useState } from "react";

const MainTotalWrapper = styled.div`
    display: flex;
    flex-direction: column;
    align-items: center;
    min-height: 200vh;
    width: 100vw;
    background-color: #FBFBFB;
    font-family: "LineRg";
`
const ContentWrapper = styled.div`
    width: 1190px; height: 100%;
    display: flex;
`

const LeftMenu = styled.div`
    width: 150px; height: 150px;
    display: flex;
    align-items: center;
    justify-content: center;
    background-color: rgba(211, 211, 211, 0.2);
    
    ul{
        padding-left: 0px;
    }

    li{
        list-style-type: none;
        cursor: pointer;
    }
`
const MainMenu = styled.div`
    width: 100%; height: auto;
    background-color: inherit;
`

export default function MyPage(){
    const nav = useNavigate();
    const [roleType, setRoleType] = useState<String>("");

    const handleNavigation = (path:string) => {
        nav(path);
    }

    const getAuth = () => {
        const token = localStorage.getItem("accessToken")

            if (!token) {
                console.error("JWT 토큰이 없습니다. 로그인 해주세요.");
                nav("/category/home");   // 로그인 되어 있지 않을때 메인페이지로 이동
                return;
            }
        return token;
    }

    useEffect(() => {
        const fetchUserRole = async () => {
            const token = getAuth();

            try {
                const response = await axios.get(process.env.REACT_APP_API_BASE_URL + "/api/v1/user/role/check", {
                    headers: {
                        Authorization: `Bearer ${token}`,
                    }
                });
                setRoleType(response.data);
            } catch(error) {
                console.error("Error fetching roleType: ", error);
            }
        }

        fetchUserRole();
    },[]);    

    return(
        <MainTotalWrapper>
            <Header/>

            <ContentWrapper>
                <LeftMenu>
                    <ul>
                        <li onClick={() => handleNavigation("/mypage/orderlist")}>주문내역</li>
                        <li onClick={() => handleNavigation("/mypage/cart")}>장바구니</li>
                        <li>개인정보 수정</li>
                        <li>배송지 관리</li>
                        {roleType === "SELLER" && (<li onClick={() => handleNavigation("/mypage/seller/itemRegistration")}>상품 등록</li>)}
                        {roleType === "SELLER" && (<li onClick={() => handleNavigation("/mypage/seller/itemManagement")}>등록 상품 관리</li>)}
                    </ul>
                </LeftMenu>

                <MainMenu>
                    <Outlet/>
                </MainMenu>
            </ContentWrapper>
            
        </MainTotalWrapper>
    )
}