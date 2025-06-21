{
 "cells": [
  {
   "cell_type": "code",
   "execution_count": 7,
   "id": "50d9bb76-b453-4b02-ad3f-7482d91f5a15",
   "metadata": {},
   "outputs": [
    {
     "name": "stdout",
     "output_type": "stream",
     "text": [
      "\n",
      "           Dr.Alice Smith,\n",
      "           Dr. Bob JohnSon,\n",
      "           Dr. Charlie Bown\n",
      "        \n"
     ]
    }
   ],
   "source": [
    "def get_doctor(query):\n",
    "    p = \"\"\"\n",
    "           Dr.Alice Smith,\n",
    "           Dr. Bob JohnSon,\n",
    "           Dr. Charlie Bown\n",
    "        \"\"\"\n",
    "    return p;\n",
    "def get_doctor_availability(doctor_name):\n",
    "    availabilty = {\n",
    "        \"Dr. Alice Smith\": \"Monday to Friday, 9 AM - 5 PM\",\n",
    "        \"Dr. Bob Johnson\": \"Tuesday to Saturday, 10 AM - 6 PM\",\n",
    "        \"Dr. Charlie Brown\": \"Monday, Wednesday, Friday, 8 AM - 4 PM\"\n",
    "    }\n",
    "    availaility = availability[doctor_name]\n",
    "    availability = str(availability)\n",
    "    return availability;\n",
    "\n",
    "tool = [\n",
    "    {\n",
    "        type: \"function\",\n",
    "        function:{\n",
    "            \"name\":\"get_doctors\",\n",
    "            \"description\":\"\"\"use this function to describe the list of doctors' names.\"\"\",\n",
    "            \"parameters\":{\n",
    "                \"type\":\"object\",\n",
    "                \"properties\":{\n",
    "                    \"query\":{\n",
    "                    \"type\":\"string\",\n",
    "                    \"description\":\"A query to get the doctors' name\n",
    "                     }  \n",
    "                }                \n",
    "            },\n",
    "             \"required\": [\"query\"],\n",
    "        },\n",
    "        type: \"function\",\n",
    "        function:{\n",
    "            \"name\":\"get_doctors_availability\",\n",
    "            \"description\":\"\"\"Use this function to get the availability of a specified doctor.\"\"\"\n",
    "            \"paramters\":{\n",
    "                \"type\":\"object\",\n",
    "                \"properties\":{\n",
    "                    \"doctor_name\":{\n",
    "                        \"type\":\"string\",\n",
    "                        \"description\":\"The name of the doctor to check availability.\",\n",
    "                }\n",
    "            },\n",
    "            \"required\":[\"doctor_name\"]\n",
    "    }"
   ]
  }
 ],
 "metadata": {
  "kernelspec": {
   "display_name": "Python 3 (ipykernel)",
   "language": "python",
   "name": "python3"
  },
  "language_info": {
   "codemirror_mode": {
    "name": "ipython",
    "version": 3
   },
   "file_extension": ".py",
   "mimetype": "text/x-python",
   "name": "python",
   "nbconvert_exporter": "python",
   "pygments_lexer": "ipython3",
   "version": "3.12.3"
  }
 },
 "nbformat": 4,
 "nbformat_minor": 5
}
