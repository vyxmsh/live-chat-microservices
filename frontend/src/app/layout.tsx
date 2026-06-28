import type { Metadata } from 'next';
import './globals.css';

export const metdata: Metadata = {
    title: ' Live Chat',
    description: 'lice Chat Application'
};

export default function Rootlayout({
    children,
}: {
    children:React.ReactNode;
}) {
    return (
        <html lang="en">
            <body>{children}</body>
        </html>
    );
}
