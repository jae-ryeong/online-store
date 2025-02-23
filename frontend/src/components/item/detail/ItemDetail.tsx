import { useEffect, useRef, useState } from "react"
import "../css/ItemDetail.css"
import { useNavigate, useParams } from "react-router-dom"
import axios from "axios"
import Header from "../../header/TotalHeader";
import useMoveScroll from "../../../hook/useMoveScrool";
import { useAuth } from "../../../hook/AuthProvider";
import { checkAuth } from "../../../hook/checkAuth";

interface ItemData {
    itemName: string;
    price: number;
    mainImageUrl: string;
    storeName: string;
    description: string;
    soldCount: number;
    soldOut: boolean;
    quantity: number; // 재고
}
export default function ItemDetail(){
    const {itemId} = useParams<{itemId:string}>();
    const [item, setItem] = useState<ItemData|null>();
    const [orderQuantity, setOrderQuantity] = useState<number>(1);
    const [activeTab, setActiveTab] = useState<string>("description");
    const {isLogin} = useAuth();
    const nav = useNavigate();

    const { elementRef: itemDescriptionRef, onMoveToElement: moveToDescription } = useMoveScroll();
    const { elementRef: reviewWrapperRef, onMoveToElement: moveToReview } = useMoveScroll();

    useEffect(() => {
        const fetchData = async() => {
            try{
                const response = await axios.get(`http://localhost:8080/api/v1/item/detail/${itemId}`)
                const result : ItemData = response.data;
                setItem(result);
            }catch(error){
                console.error("API 호출 실패: ", error);
            }        
        };

        fetchData();
    },[]);

    useEffect(() => {
        const observerOptions = {
            root: null,
            rootMargin: "-50% 0px -50% 0px",    // 리뷰 창이 화면 중간에 오면 active로 변경
            threshold: 0,
        };

        const observerCallback = (entries: IntersectionObserverEntry[]) => {
            entries.forEach((entry) => {
                if(entry.isIntersecting){
                    if(entry.target === itemDescriptionRef.current){
                        setActiveTab("description");
                    }else if(entry.target === reviewWrapperRef.current){
                        setActiveTab("review");
                    }
                }
            });
        };

        const observer = new IntersectionObserver(observerCallback, observerOptions);

        if(itemDescriptionRef.current) observer.observe(itemDescriptionRef.current);
        if(reviewWrapperRef.current) observer.observe(reviewWrapperRef.current);

        return () => {
            if(itemDescriptionRef.current) observer.unobserve(itemDescriptionRef.current);
            if(reviewWrapperRef.current) observer.unobserve(reviewWrapperRef.current);
        }
    },[]);

    //장바구니 추가 함수
    const handleAddToCart = async() => {
        if(!isLogin){
            alert("로그인이 필요한 서비스입니다.");
            nav("/login");
            return;
        }
        try{
            await axios.post(`http://localhost:8080/api/v1/user/cart/add`,{
                itemId: itemId,
                quantity: orderQuantity
            },{
                headers: {
                    Authorization: `Bearer ${localStorage.getItem("accessToken")}`,
                    "Content-Type": "application/json",
                    
                }
            }
            );
            alert("장바구니에 추가되었습니다.");
        }catch(error){
            console.error("API 호출 실패: ", error);
        }
    }

    const handleInputChange = (event: React.ChangeEvent<HTMLInputElement>) => {
        setOrderQuantity(Number(event.target.value));
    };

    return(
        <div className="totalWrapper">
            <Header/>
            <div className="itemDetailWrapper">
                <div className="itemWrapper">
                    <img className="mainImg" src={`${item?.mainImageUrl}`} alt={`${item?.itemName}`}/>
                </div>

                <div className="itemBuyWrapper">
                    <div className="itemInfo">

                        <h1 className="storeName">{item?.storeName}</h1>
                        <h1 className="itemName">{item?.itemName}</h1>

                        <p className="itemPrice">{item?.price}원</p>
                        <p className="itemQuantity">재고: {item?.quantity}</p>
                    </div>

                    <div className="itemOrderWrapper">

                        <div className="quantityWrapper">
                            <button className="quantityButton" onClick={() => setOrderQuantity(Math.max(1,orderQuantity - 1))}>-</button>
                            <input type="text" className="quantityInput" value={orderQuantity} onChange={handleInputChange} maxLength={6}/>
                            <button className="quantityButton" onClick={() => setOrderQuantity(orderQuantity + 1)}>+</button>
                        </div>

                        <button className="cartButton" onClick={handleAddToCart}>장바구니 담기</button>
                    </div>
                </div>
            </div>

            <div className="contentWrapper">
                <div className="tab-container">
                    <div className={`tab ${activeTab === "description" ? "active" : ""}`} onClick={moveToDescription}>상세정보</div>
                    <div className={`tab ${activeTab === "review" ? "active" : ""}`} onClick={moveToReview}>리뷰</div>
                </div>

                <div className="itemDescription" ref={itemDescriptionRef}>
                    {item ? <div dangerouslySetInnerHTML={{__html: item.description}}></div> : null}
                </div>

                <div className="reviewWrapper" ref={reviewWrapperRef}>

                </div>
            </div>

            
            

        </div>
    )
}