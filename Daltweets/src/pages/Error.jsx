import { Link } from "react-router-dom";

function Error(){
  return (
    <div> 
     <section className="bg-white ">
    <div className="py-8 px-4 mx-auto max-w-screen-xl lg:py-16 lg:px-6">
        <div className="mx-auto max-w-screen-sm text-center">
            <h1 className="mb-4 text-7xl tracking-tight font-extrabold lg:text-9xl text-primary-600">Error!</h1>
            <p className="mb-4 text-lg font-light text-gray-500">Please try to login again.</p>
            <Link to="/login" className="text-yellow-500 hover:text-yellow-700">
            Login
        </Link>
        </div>   
    </div>
</section>
    </div> 
  )
};

export default Error;
