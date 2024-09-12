import { Component, ReactNode } from "react";
import styled from "styled-components";
import Header from "../components/header";

const TotalWrapper = styled.div`
    
    height: 100vh;
    width: 100vw;
    background-color: black;
`

export default function Home() {
    return(
        <TotalWrapper>
            <Header/>
        </TotalWrapper>
    );
}