import { Component, ReactNode } from "react";
import styled from "styled-components";
import Header from "../components/header/header";

const TotalWrapper = styled.div`
    display: flex;
    flex-direction: column;
    align-items: center;
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