import { useState } from "react";
import "../../css/management.css"
import TextEditor from "./textEditor";
import axios from "axios";
import { useNavigate } from "react-router-dom";

{/* 이미지 크기 제한

파일 형식 제한

가격 입력 시 숫자 포맷팅

Drag & Drop 파일 업로드 기능
 */}
type Category = 'BOOK' | 'FOOD' | 'CLOTHES' | 'PET';
interface ItemFormProps {
    itemName: string;
    price: number;
    quantity: number;
    category: Category;
    mainImageUrl: string;
    description: string;
}

export default function ItemForm() {
    const [formData, setFormData] = useState<ItemFormProps>({
        itemName: '',
        price: 0,
        mainImageUrl: '',
        quantity: 0,
        category: 'BOOK',
        description: "",
    });
    const [mainImagePreview, setMainImagePreview] = useState<string | null>(null);
    const nav = useNavigate();

    // Input에 값을 입력하면 formData Change
    const handleInputChange = (e: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement | HTMLSelectElement>) => {
        const { name, value } = e.target;
        setFormData((prev) => ({
            ...prev,
            [name]: name === 'price' || name === 'quantity' ? Number(value) : value
        }));
    }

    // 메인 이미지 핸들러
    const handleMainImageChange = async (e: React.ChangeEvent<HTMLInputElement>) => {
        if (e.target.files && e.target.files[0]) {
            const formData = new FormData();
            const file = e.target.files[0];
            formData.append('file', file);

            // 기존 이미지 존재시 삭제
            if(mainImagePreview) {
                const fileName = mainImagePreview.split('/').pop();
                try {
                    await axios.delete(`http://localhost:8080/api/v1/item/gcp/image/delete/${fileName}`);
                    setMainImagePreview(null);
                    setFormData((prev) => ({...prev, mainImageUrl: ''}))
                } catch(error){
                    console.error("Failed to delete image", error);
                }
            }

            // 이미지 업로드
            try {
                const response = await axios.post("http://localhost:8080/api/v1/item/gcp/image/upload", formData, {
                    headers: {"Content-Type": "multipart/form-data"},
                });
                setFormData(prev => ({ ...prev, mainImageUrl: decodeURI(response.data)}));
                setMainImagePreview(decodeURI(response.data));
                return decodeURI(response.data);
            } catch(error){
                console.error("Image upload failed: ", error);
                return ""
            }        
        }            
    }

    // description 이미지 업로드: GCP 저장 -> URL 반환
    const handleImageUpload = async (file: File): Promise<string> => {
        const formData = new FormData();
        formData.append('file', file);
        
        try{
            const response = await axios.post("http://localhost:8080/api/v1/item/gcp/image/upload", formData, {
                headers: {"Content-Type": "multipart/form-data"},
            });         
            return decodeURI(response.data);
        } catch(error){
            console.error("Image upload failed: ", error);
            return ""
        }
    }

    // 에디터에 이미지 삭제시 GCP에서 삭제
    const deletedImages = async (newContent: string) => {
        const parser = new DOMParser();
        const newDoc = parser.parseFromString(newContent, "text/html");
        const newImages = Array.from(newDoc.querySelectorAll("img")).map((img) => img.src);

        const oldDoc = parser.parseFromString(formData.description, "text/html");
        const oldImages = Array.from(oldDoc.querySelectorAll("img")).map((img) => img.src);

        // oldImages 배열에서 newImages 배열에 포함되지 않은 이미지 URL을 필터링
        const deletedImages = oldImages.filter((url) => !newImages.includes(url));

        for (const imageUrl of deletedImages) {
            const fileName = imageUrl.split('/').pop();
            try {
                await axios.delete(`http://localhost:8080/api/v1/item/gcp/image/delete/${fileName}`);
            } catch(error){
                console.error("Failed to delete image", error);
            }
        }
    };

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();
        try {
            const response = await axios.post("http://localhost:8080/api/v1/item/registration", formData)
        } catch(error){
            console.error("Item registration failed: ", error);
        }
        nav("/category/home");
    }

    // 'onChange' 함수가 리렌더링될 때마다 새로 생성되는 것을 방지
    const handleDescriptionChange = (value:string) => {
        deletedImages(value);
        setFormData(prev => ({...prev, description: value}));
    };

    return (
        <div className="FormTotalWrapper">
            <h1>상품 등록</h1>
            <form>
                {/* 상품명 */}
                <div className="form-group">
                    <label>상품명</label>
                    <input type="text" name="itemName" value={formData.itemName} onChange={handleInputChange} required />
                </div>

                {/* 대표이미지 220 * 220 */}  
                <div className="form-group">
                    <label>대표 이미지</label>
                    <input type="file" accept="image/*" onChange={handleMainImageChange} required />
                    {mainImagePreview && (
                        <div className="image-preview">
                            <img src={mainImagePreview} alt="메인 이미지 미리보기" />
                        </div>
                    )}
                </div>

                {/* 판매 가격 */}
                <div className="form-group">
                    <label>판매 가격</label>
                    <input type="number" name="price" value={formData.price} onChange={handleInputChange} required />
                </div>

                {/* 판매 수량 */}
                <div className="form-group">
                    <label>판매 수량</label>
                    <input type="number" name="quantity" value={formData.quantity} onChange={handleInputChange} required />
                </div>

                {/* 카테고리 선택 */}
                <div className="form-group">
                    <label>카테고리</label>
                    <select name="category" value={formData.category} onChange={handleInputChange}>
                        <option value="BOOK">도서</option>
                        <option value="FOOD">음식</option>
                        <option value="CLOTHES">의류</option>
                        <option value="PET">펫용품</option>
                    </select>
                </div>

                {/* 상세 설명 */}
                <div className="form-group">
                    <label>상세 설명</label>
                    <TextEditor
                        value={formData.description}
                        onChange={handleDescriptionChange}
                        onImageUpload={handleImageUpload}
                        placeholder="상품 상세 설명을 작성해주세요." />
                </div>

                {/* 제출 버튼 */}
                <button type="submit" onClick={handleSubmit}>상품 등록</button>
            </form>
        </div>
    )
}
