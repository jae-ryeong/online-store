import axios from "axios";
import { useEffect, useState } from "react";
import { useLocation, useNavigate, useParams } from "react-router-dom";
import styled from "styled-components";
import PaginationComponent from "./pagination";

const Wrapper = styled.div`
        background-color: white;
        width: 1190px;
        flex: 1 0 auto;
        flex-direction: row;
        align-items: center;
        justify-content: center;        
    `

const Sorting = styled.div`
    width: 100%;
    height: 40px;  
    background-color: #EEEEEE ;
`;

const ItemWrapperWrpper = styled.div`
    display:grid;
    grid-template-columns: repeat(4, 1fr);
    grid-template-rows: repeat(5, auto);
    gap: 10px;
    place-items: center;
    padding-top: 50px;

    cursor: pointer;
    
`

const ItemWrpper = styled.div`
    width: 220px;
    height: 350px;
    //display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
    padding: 0px 30px;
    
    overflow: hidden;
    transition: transform 0.2s ease;
`

const PhotoWrapper = styled.img`
    width: 100%;
    height: 220px;
    object-fit: cover; // 이미지 비율 유지하면서 크기 조정
`
const ItemName = styled.p`
    width: 100%;
    min-height: 30px;
    height: auto;
    font-size: 20px;

    color: #333;
    margin-top: -30px;
    line-height: 1.3;
`
const ItemPrice = styled.p`
    width: 100%;
    height: 30px;
    font-size: 20px;
    font-weight: bold;
    margin-top: -50px;
`
interface Item {
    itemName: String;
    price: number;
    mainImageUrl: string;
    itemId: number;
}

interface PageData {
    content: Item[];
    totalPages: number;
    totalElements: number;
    size: number;
    number: number;
}

interface ContentsProps{
    page:number;
    setPage: React.Dispatch<React.SetStateAction<number>>;
}

export default function Contents({page, setPage}:ContentsProps) { 
    const nav = useNavigate();
    const location = useLocation();
    const [data, setData] = useState<PageData | null>(null);

    useEffect(() => {
        const category = location.pathname.split('/')[2] // URL에서 category 추출
        if(!category) return;
        
        const fetchData = async() => {
            try{
                let apiUrl = "";    
                switch(category) {
                    case "book":
                        apiUrl = process.env.REACT_APP_API_BASE_URL + `/api/v1/item/find/book?page=${page}`;
                        break;
                    case "food":
                        apiUrl = process.env.REACT_APP_API_BASE_URL + `/api/v1/item/find/food?page=${page}`;
                        break;
                    case "clothes":
                        apiUrl = process.env.REACT_APP_API_BASE_URL + `/api/v1/item/find/clothes?page=${page}`;
                        break;
                    case "pet":
                        apiUrl = process.env.REACT_APP_API_BASE_URL + `/api/v1/item/find/pet?page=${page}`;
                        break;
                    case "home":
                        apiUrl = process.env.REACT_APP_API_BASE_URL + `/api/v1/item/search?page=${page}`;
                        break;
                    default:
                        apiUrl = "error";
                }
    
                const response = await axios.get(apiUrl);
                const result: PageData = response.data;
                setData(result);
            } catch (error){
                console.error("API 호출 실패: ", error);
            }
        };
        fetchData();
    }, [location.pathname, page]);

    const clickHandler = (itemId:number) => {
        nav(`/detail/${itemId}`)
    }
    
    return (
        <Wrapper>
            <Sorting className="sorting"></Sorting>

            <ItemWrapperWrpper>
                {data?.content.map((item) => (
                    <ItemWrpper className="itemWrapper" key={item.itemId} onClick={() => clickHandler(item.itemId)}>
                        <PhotoWrapper className="photoWrapper" src={item.mainImageUrl}></PhotoWrapper>
                        <ItemName className="itemName">{item.itemName}</ItemName>
                        <ItemPrice className="itemPrice">{item.price}원</ItemPrice>
                    </ItemWrpper>
                ))}
            </ItemWrapperWrpper>

            {/* 페이지네이션 버튼 */}
            <PaginationComponent page={page} setPage={setPage} totalElements={ data ? data.totalElements : 1} totalPages={data ? data.totalPages : 1}/>

        </Wrapper>
    )
}