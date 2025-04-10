import "../css/Card.css"

interface Order {
    date: string;
    status: string;
    deliveryDate: string;
    items: {
        name: string;
        price: number;
        quantity: number;
    }[];
    buttons: string[] // (배송조회, 주문취소 등)
}

const orders: Order[] = [
    {
        date: "2025. 1. 8",
        status: "상품준비중",
        deliveryDate: "1/20(월) 도착 예정",
        items: [
            {
                name: "커피잔 세트",
                price: 10000,
                quantity: 2
            }
        ],
        buttons: ["배송조회", "주문취소", "판매자 문의"]
    },
    {
        date: "2025. 1. 5",
        status: "배송완료",
        deliveryDate: "1/5(일) 도착 예정",
        items: [
            {
                name: "라면 세트",
                price: 40000,
                quantity: 3
            }
        ],
        buttons: ["배송조회", "교환, 반품 신청", "리뷰 작성하기"]
    }
];

const OrderItem: React.FC<{ order: Order }> = ({ order }) => {

    return (
        <div className="CardTotalWrapper">
            <div className="CardWrapper">
                <h3>{order.date} 주문</h3>
                <p>{order.status} - {order.deliveryDate}</p>
                {order.items.map((item, index) => (
                    <div key={index} style={{ marginBottom: "10px" }}>
                        <p>{item.name}</p>
                        <p>
                            {item.price.toLocaleString()} 원 - {item.quantity}개
                        </p>
                    </div>
                ))}
                <div style={{ display: "flex", gap: "10px" }}>
                    {order.buttons.map((button, index) => (
                        <button key={index} style={{ padding: "5px 10px" }}>{button}</button>
                    ))}
                </div>
            </div>
        </div>
    )
}


export default function OrderList(){
    return (
        <div className="TotalWrapper">
            {orders.map((order, index) => (
                <OrderItem key={index} order={order} />
            ))}
        </div>
    )
}