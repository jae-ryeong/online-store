import styled from "styled-components";
import Header from "./header/TotalHeader";
import Banner from "./banner/TotalBanner";
import Category from "./CategoryMenu/TotalCategory";
import Contents from "./content/contents";

const MainTotalWrapper = styled.div`
    display: flex;
    flex-direction: column;
    align-items: center;
    height: 200vh;
    width: 100vw;
    background-color: #FBFBFB;
`

export default function MainComponent(){
    return(
    <MainTotalWrapper>
        <Header/>
        <Banner/>
        <Category/>
        <Contents/>
    </MainTotalWrapper>
);}