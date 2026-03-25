export default function Button({text , onClick}) {
  return (
  <div className='flex justify-end'> 
    <button
      onClick={onClick}
      className='px-6 py-2 bg-gray-800/30 text-white border-2 font-semibold rounded-xl shadow-lg hover:bg-gray-800 transition-colors'>
      {text}
    </button> 
  </div>
  );
}
