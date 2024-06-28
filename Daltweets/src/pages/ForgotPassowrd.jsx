import { useState } from "react";
import axios from "axios";

function ForgotPassword() {
  const [securityQ, setSecurityQ] = useState("");
  const [username, setUsername] = useState("");
  const [qError, setQError] = useState(false);
  const [securityA, setSecurityA] = useState("");
  const [newPassword, setNewPassword] = useState("");
  const [securityAError, setSecurityAError] = useState(false);
  const [securityAErrorMessage, setSecurityAErrorMessage] = useState("");
  const [passwordChanged, setPasswordChanged] = useState(false);

  const handleUserSubmit = (e) => {
    e.preventDefault();

    if (username == "") {
      setQError(true);
      return;
    } else {
      setQError(false);
    }
    axios
      .get(
        `${import.meta.env.VITE_BACKEND_BASE_URL}/api/login/get-security-question/${username}`,
      )
      .then((response) => {
        setSecurityQ(response.data);
        setQError(false);
      })
      .catch((e) => {
        console.log(e);
        setQError(true);
      });
  };

  const handleSecuritySubmit = (e) => {
    e.preventDefault();

    if (securityA == "") {
      setSecurityAError(true);
      setSecurityAErrorMessage("Must include a security answer.");
      return;
    } else if (newPassword == "") {
      setSecurityAError(true);
      return;
    }

    axios
      .post(
        `${import.meta.env.VITE_BACKEND_BASE_URL}/api/login/forgot-password-change`,
        {
          username: username,
          "new-password": newPassword,
          "security-answer": securityA,
        },
      )
      .then(() => {
        setSecurityAError(false);
        setPasswordChanged(true);
      })
      .catch((e) => {
        console.log(e);
        setSecurityAError(true);
        setSecurityAErrorMessage("Security answer is incorrect");
        setPasswordChanged(false);
      });
  };

  return (
    <div className="flex min-h-full flex-1 flex-col justify-center px-6 py-12 lg:px-8">
      <div className="box-border my-20 mx-52">
        <div className="mt-10 lg:mx-auto lg:w-full lg:max-w-sm">
          <h2 className="mt-10 text-left text-2xl font-bold leading-9 tracking-tight text-gray-900">
            Forgot Password
          </h2>
          <br />
          <form>
            <div className="w-full">
              {passwordChanged && (
                <div>
                  Password changed Successfully please got to{" "}
                  <a href="/login">Login Page</a>
                </div>
              )}

              {!passwordChanged && securityQ && (
                <div>
                  <div>
                    <h3 className="p-2 font-bold">{securityQ}</h3>
                  </div>
                  <div className="w-full p-2">
                    <label
                      htmlFor="security-answer"
                      className="block text-sm font-medium leading-6 text-gray-900"
                    >
                      Security Answer
                    </label>
                    <div>
                      <input
                        id="security-answer"
                        onChange={(e) => {
                          setSecurityA(e.target.value);
                        }}
                        className="block w-full rounded-md border-0 py-1.5 ps-2 text-gray-900 shadow-sm ring-1 ring-inset ring-gray-300 placeholder:text-gray-400 focus:ring-2 focus:ring-inset focus:ring-indigo-600 sm:text-sm sm:leading-6"
                      />
                    </div>
                    {securityAError && <div>{securityAErrorMessage}</div>}
                  </div>
                  <div className="w-full p-2">
                    <label
                      htmlFor="new-password"
                      className="block text-sm font-medium leading-6 text-gray-900"
                    >
                      New Password
                    </label>
                    <div>
                      <input
                        id="new-password"
                        onChange={(e) => {
                          setNewPassword(e.target.value);
                        }}
                        className="block w-full rounded-md border-0 py-1.5 ps-2 text-gray-900 shadow-sm ring-1 ring-inset ring-gray-300 placeholder:text-gray-400 focus:ring-2 focus:ring-inset focus:ring-indigo-600 sm:text-sm sm:leading-6"
                      />
                    </div>

                    {securityAError && newPassword == "" && (
                      <div>Must submit a new password. </div>
                    )}
                    {}
                  </div>

                  <button
                    type="submit"
                    onClick={handleSecuritySubmit}
                    className="mt-2 flex w-min justify-center rounded-md bg-black px-3 py-1.5 text-sm font-semibold leading-6 text-yellow-500 shadow-sm hover:bg-yellow-500 hover:text-black focus-visible:outline focus-visible:outline-2 focus-visible:outline-offset-2 focus-visible:outline-indigo-600"
                  >
                    Submit
                  </button>
                </div>
              )}

              {!securityQ && (
                <div className="w-max">
                  <h3>Please enter the username of the account.</h3>
                  <label
                    htmlFor="username"
                    className="block text-sm font-medium leading-6 text-gray-900"
                  >
                    Username
                  </label>
                  <div>
                    <input
                      id="username"
                      onChange={(e) => {
                        setUsername(e.target.value);
                      }}
                      className="block w-full rounded-md border-0 py-1.5 ps-2 text-gray-900 shadow-sm ring-1 ring-inset ring-gray-300 placeholder:text-gray-400 focus:ring-2 focus:ring-inset focus:ring-indigo-600 sm:text-sm sm:leading-6"
                    />
                    {qError && <div>Username not found</div>}
                  </div>

                  <button
                    type="submit"
                    onClick={handleUserSubmit}
                    className="mt-2 flex w-min justify-center rounded-md bg-black px-3 py-1.5 text-sm font-semibold leading-6 text-yellow-500 shadow-sm hover:bg-yellow-500 hover:text-black focus-visible:outline focus-visible:outline-2 focus-visible:outline-offset-2 focus-visible:outline-indigo-600"
                  >
                    Submit
                  </button>
                </div>
              )}
            </div>
          </form>
        </div>
      </div>
    </div>
  );
}

export default ForgotPassword;
