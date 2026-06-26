interface PresenceDotProps{
    online: boolean;
}

export default function PresenceDot({online}: PresenceDotProps) {
    return (
        <span 
          className ={`inline-block w-2.5 h-2.5 rounded-full flex-shrink-0 ${ online ? `bg-green-500` : `bg-gray-400` }`}
            title = {online ? `Online`:`Offline`}
        />
    );
}