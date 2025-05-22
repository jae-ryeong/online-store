import { useEffect, useState } from "react";
import "../../css/Card.css"
import { useNavigate } from "react-router-dom";

interface CartItem {
    itemCartId: number;
    itemName: string;
    storeName: string;
    quantity: number;
    price: number;
    cartCheck: boolean;
    mainImageUrl: string;
}
const NoCartPage: React.FC = () => {
    return(
        <div className="CardTotalWrapper align_items_center">
            장바구니가 비어있습니다.
        </div>
    )
}

const CartCard2: React.FC = () => {
    const nav = useNavigate();
    const [cartItems, setCartItems] = useState<CartItem[]>([]);

    const getAuth = () => {
        const token = localStorage.getItem("accessToken")

            if (!token) {
                console.error("JWT 토큰이 없습니다. 로그인 해주세요.");
                nav("/category/home");   // 로그인 되어 있지 않을때 메인페이지로 이동
                return;
            }
        return token;
    }

    // 로컬스토리지 jwt로 서버로부터 장바구니 데이터 받아오기
    useEffect(() => {
        const fetchCartData = async () => {
            const token = getAuth();

            try {
                const response = await fetch(process.env.REACT_APP_API_BASE_URL + "/api/v1/user/cart/view", {
                    method: "GET",
                    headers: {
                        "Content-Type": "application/json",
                        Authorization: `Bearer ${token}`, // Authorization 헤더에 토큰 추가
                    },
                });

                if (response.ok) {
                    const data = await response.json();
                    setCartItems(data); // 받아온 데이터를 상태에 저장
                } else {
                    console.error("서버 응답 에러:", response.status);
                }
            } catch (error) {
                console.error("API 호출 에러:", error);
            }
        };

        fetchCartData();
    }, []);

    // 체크박스 핸들러
    const handleCheckboxChange = async (itemCartId: number) => {
        const token = getAuth();
        try{
            const response = await fetch(process.env.REACT_APP_API_BASE_URL + "/api/v1/user/cart/view/check", {
                method: "PUT",
                headers: {
                    "Content-Type": "application/json",
                    Authorization: `Bearer ${token}`,
                },
                body: JSON.stringify({
                    itemCartId: itemCartId
                }),
            });
            
            if (!response.ok) {
                throw new Error(`Failed to update item with ID ${itemCartId}`);
            }

            setCartItems((prevItems) => prevItems.map((item) => item.itemCartId === itemCartId ? {...item, cartCheck: !item.cartCheck} : item));

        } catch(error){
            console.error("Failed to update checked state:", error);
            alert("체크 상태를 변경하는 데 실패했습니다.");
        }
    };

    // 수량 증가
    const handleQuantityUp = async(itemCartId: number) => {
        const token = getAuth();
        try{
            const response = await fetch(process.env.REACT_APP_API_BASE_URL + `/api/v1/user/cart/view/${itemCartId}/up`, {
                method: "PUT",
                headers: {
                    "Content-Type": "application/json",
                    Authorization: `Bearer ${token}`,
                },
            })

            setCartItems((prevItems) => prevItems.map((item) => item.itemCartId === itemCartId ? {...item, quantity: item.quantity+ 1} : item));
        } catch(error){
            console.error("Failed to update Quantity:", error);
            alert("수량을 변경하는 데 실패했습니다.");
        }
    }

    // 수량 감소
    const handleQuantityDown = async(itemCartId: number) => {
        const currentItem = cartItems.find((item) => item.itemCartId === itemCartId);

        if (!currentItem || currentItem.quantity <= 1) {
            alert("최소 수량은 1개입니다.");
            return;
        }
        const token = getAuth();

        try{
            const response = await fetch(process.env.REACT_APP_API_BASE_URL + `/api/v1/user/cart/view/${itemCartId}/down`, {
                method: "PUT",
                headers: {
                    "Content-Type": "application/json",
                    Authorization: `Bearer ${token}`,
                },
            })

            setCartItems((prevItems) => prevItems.map((item) => item.itemCartId === itemCartId ? {...item, quantity: item.quantity - 1} : item));
        } catch(error){
            console.error("Failed to update Quantity:", error);
            alert("수량을 변경하는 데 실패했습니다.");
        }
    }

    // 체크된 상품 필터링
    const checkedItems = cartItems.filter((item) => item.cartCheck);

    // 총 상품 수 계산
    const totalQuantity = checkedItems.reduce((sum, item) => sum + item.quantity, 0);

    // 총 상품 가격 계산
    const totalPrice = checkedItems.reduce((sum, item) => sum + item.quantity * item.price, 0);

    // 결제하기 버튼 클릭
    const handlePayment = async() => {
    nav("/cart/checkout");
    }

    // 삭제 버튼
    const handleDelete = async(itemCartId: number) => {
        const token = getAuth();
        try{
            const response = await fetch(process.env.REACT_APP_API_BASE_URL + `/api/v1/user/cart/view/delete/${itemCartId}`, {
                method: "DELETE",
                headers: {
                    "Content-Type": "application/json",
                    Authorization: `Bearer ${token}`,
                },
            })

            if (!response.ok) {
                throw new Error(`Failed to delete item with ID ${itemCartId}`);
            }

            setCartItems((prevItems) => prevItems.filter((item) => item.itemCartId !== itemCartId));
            
            window.location.reload();
        } catch(error){
            console.error("Failed to delete item:", error);
            alert("상품을 삭제하는 데 실패했습니다.");
        }
    }

    return (
        <div className="TotalWrapper flex">
            {cartItems.length === 0 ? <NoCartPage/> : (
                <div>
                    {cartItems.map((item, index) => (
                        <div className="CardTotalWrapper flex" key={index}>

                            <div className="CardWrapper flex">
                                <div className="checkWrapper">
                                    <input className="checkBox" type="checkbox" checked={item.cartCheck} onChange={() => handleCheckboxChange(item.itemCartId)}/>
                                    <img src={item.mainImageUrl} alt="상품이미지" className="mainImage"/>
                                </div>

                                <div className="productWrpper">
                                    <h3>{item.itemName}</h3>
                                    <h4>{item.storeName}</h4>
                                    <p>{item.price.toLocaleString()}원</p>

                                    <div className="quantityButton">
                                        <button onClick={() => handleQuantityDown(item.itemCartId)}>-</button>
                                        <span className="quantity">{item.quantity}</span>
                                        <button onClick={() => handleQuantityUp(item.itemCartId)}>+</button>
                                    </div>
                                </div>

                                <div className="cancelWrapper">
                                    <div className="deleteButton" onClick={()=>handleDelete(item.itemCartId)}>삭제</div>
                                </div>
                            </div>
                        </div>
                    ))}
                </div>

            )
            }

            <div className="summary">
                <h2>결제정보</h2>
                <div className="cartTotalQuantity flex-between">
                    <h4>총 상품 수</h4>
                    <h4>{totalQuantity} 개</h4>
                </div>

                <div className="cartTotalPrice flex-between">
                    <h4>상품 금액</h4>
                    <h4>{totalPrice.toLocaleString()} 원</h4>
                </div>

                <button className="summaryButton" onClick={()=> handlePayment()}>결제하기</button>

            </div>

        </div>
    )
}

export default CartCard2;