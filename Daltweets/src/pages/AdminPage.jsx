import { Tab, TabGroup, TabList, TabPanels, TabPanel } from "@headlessui/react";
import SignupRequests from "../components/SignupRequests";
import UserList from "../components/UserList";

export default function AdminPage() {
  const tabs = [
    {
      title: "Signup Requests",
    },
    {
      title: "Users",
    },
    {
      title: "Groups",
    },
  ];
  return (
    <>
      <TabGroup className="mx-auto w-full h-min">
        <TabList className="box-border m-auto my-5 w-full space-x-4 flex justify-center">
          <div className="w-1/3 flex justify-between mr-12 pr-12">
            {tabs.map((t, idx) => {
              return (
                <Tab
                  key={idx}
                  className="rounded-full py-1 px-3 text-sm/6 font-semibold focus:outline-none data-[selected]:bg-gold/50 data-[hover]:bg-gold/50 data-[selected]:data-[hover]:bg-gold/50 data-[focus]:outline-1 data-[focus]:outline-white"
                >
                  {t.title}
                </Tab>
              );
            })}
          </div>
        </TabList>
        <TabPanels className="box-border w-full h-max">
          <TabPanel className="box-border w-full">
            <SignupRequests />
          </TabPanel>
          <TabPanel>
            <UserList />
          </TabPanel>
          <TabPanel></TabPanel>
        </TabPanels>
      </TabGroup>
    </>
  );
}
