export default function Input({ placeholder, type="text" , value , onChange }) {
  return (
    <div className="flex justify-center">
      <input
        type={type}
        value={value}
        onChange={onChange}
        placeholder={placeholder}
        className="w-96 px-8 py-2.5 border-2 border-gray-300 rounded-lg
        focus:outline-none focus:border-black transition duration-200 font-bold"
      />
    </div>
  );
}