import { Tab, TabGroup, TabList, TabPanels, TabPanel } from "@headlessui/react";
import SignupRequests from "../components/SignupRequests";
import UserList from "../components/UserList";

export default function AdminPage() {
  return (
    <>
      <TabGroup className="mx-auto w-full h-min">
        <TabList className="box-border m-auto w-10/12 space-x-4 flex justify-center">
          <Tab>Signup Requests</Tab>
          <Tab>Users</Tab>
          <Tab>Groups</Tab>
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
