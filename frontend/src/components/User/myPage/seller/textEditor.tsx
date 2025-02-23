import { memo, useMemo, useRef } from "react"
import ReactQuill from "react-quill"
import '../../css/TextEditor.css'
import 'react-quill/dist/quill.snow.css';

interface TextEditorProps {
    value: string;
    onChange: (value: string) => void;
    onImageUpload: (file:File) => Promise<string>;
    placeholder?: string;
}

const TextEditor = memo(({
    value, 
    onChange, 
    onImageUpload, 
    placeholder = '내용을 입력하세요...'
}: TextEditorProps) => {

    const quillRef = useRef<ReactQuill>(null);

    const modules = useMemo(() =>{
        return{
            toolbar: {
                container: [
                    [{header: [1, 2, 3, false]}],
                    ['bold', 'italic', 'underline', 'strike'],  // 글자 효과
                    [{list: 'ordered'}, {list: 'bullet'}],
                    ['link', 'image'],
                    ['clean']
                ],
                handlers: {
                    image: imageHandler,
                }
            }
        }
        
    }, []);

    const formats = [
        'header',
        'bold', 'italic', 'underline', 'strike',
        'list', 'bullet',
        'link', 'image'
    ];

    // 이미지 핸들러 (에디터에 이미지 삽입)
    function imageHandler() {
        const input = document.createElement('input');
        input.setAttribute('type', 'file');
        input.setAttribute('accept', 'image/*');
        input.click();

        input.onchange = async () => {
            if(input.files) {
                const file = input.files[0];
                const imageUrl = await onImageUpload(file)
                if(imageUrl) {
                    const quill = document.querySelector(".ql-editor") as HTMLElement;
                    quill.innerHTML += `<img src="${imageUrl}" alt="uploaded image" />`;
                }
            }
        };
    };

    return(
        <div className="textEditor">
            <ReactQuill ref={quillRef}
            value={value}
            onChange={onChange}
            modules={modules}
            formats={formats}
            placeholder={placeholder}/>
        </div>
    )
});

export default TextEditor;