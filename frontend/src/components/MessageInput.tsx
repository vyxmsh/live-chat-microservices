'use client';

import {useState, KeyboardEvent} from 'react';

interface MessageInputProps{
    onSend: (content: string) => void;
    disabled?: boolean;
}

export default function MessageInput({onSend, disabled}:MessageInputProps){
    const[text, setText] = useState('');

    const handleSend = () => {
        const trimmed = text.trim();
        if(!trimmed) return;
        onSend(trimmed);
        setText('');
    };

    const handleKeyDown = (e: KeyboardEvent<HTMLTextAreaElement>) => {
        //Send on Enter, newline on Shift + Enter
        if(e.key === 'Enter' && !e.shiftKey)
        {
            e.preventDefault();
            handleSend();
        }
    };

    return(
        <div className="p-4 border-t border-gray-700 bg-gray-800">
          <div className="flex items-end gap-2">
             <textarea 
              className="flex-1 bg-gray-700 text-white rounded-lg px-4 py-2 resize-none
                         focus:outline-none focus:ring-2 focus:ring-indigo-500
                         placeholder-gray-400 text-sm"
              rows ={1}
              placeholder = {disabled? 'Connecting...' : 'Type a message...'}
              value = {text}
              onChange={(e) => setText(e.target.value)}
              onKeyDown = {handleKeyDown}
              disabled={disabled}      
              />    

              <button
                onClick={handleSend}
                disabled={disabled || !text.trim()}
                className = "bg-indigo-600 hover:bg-indigo-700 disabled:opacity-40 text-white px-4 py-2 rounded-lg text-sm front-medium transition-colors"
                >
                
                Send
                </button>
            </div>
        </div>
    );
}