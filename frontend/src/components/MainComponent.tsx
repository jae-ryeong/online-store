import styled from "styled-components";
import Header from "./header/TotalHeader";
import Banner from "./banner/TotalBanner";
import Category from "./CategoryMenu/TotalCategory";
import { Outlet } from "react-router-dom";

const MainTotalWrapper = styled.div`
    display: flex;
    flex-direction: column;
    align-items: center;
    min-height: 200vh;
    width: 100vw;
    background-color: #FBFBFB;
`

interface MainComponentProps {
    setPage: React.Dispatch<React.SetStateAction<number>>
}

export default function MainComponent({setPage}:MainComponentProps){
    return(
    <MainTotalWrapper>
        <Header/>
        <Banner/>
        <Category setPage={setPage}/>
        <Outlet/>
    </MainTotalWrapper>
);}