import styled from "styled-components"
import { MdArrowForwardIos, MdOutlineArrowBackIos  } from "react-icons/md";

const BannerTotalWrapper = styled.div`
    display: flex;
    align-items: center;
    justify-content: space-between;
    width: 1190px;
    height: 450px;
    background-color: #d6bcbceb;

    .preBanner{
        font-size: 80px;
        cursor: pointer;
        margin-left: 30px;
        color: #555;
        opacity: 0.5;
        filter: drop-shadow(10px 10px 5px rgb(0, 0, 0, 0.2));
    }
    .nextBanner{
        font-size: 80px;
        cursor: pointer;
        margin-right: 30px;
        color: #555;
        opacity: 0.5;
        filter: drop-shadow(10px 10px 5px rgb(0, 0, 0, 0.2));
    }
`;

export default function Banner() {
    return(
        <BannerTotalWrapper>
            <MdOutlineArrowBackIos className="preBanner"/>
            <MdArrowForwardIos className="nextBanner" />
        </BannerTotalWrapper>
    );
}