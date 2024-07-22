require('dotenv').config();
let fs = require("fs"),
    path = require("path"),
    OpenAI = require("openai");

const openai = new OpenAI();

async function main() {
    const completion = await openai.chat.completions.create({
        messages: [
            { "role": "system", "content": "You are a helpful assistant." },
            //{ "role": "user", "content": "Generate 10 java spring boot microservices interview questions whose answers are descriptive and evaluates whether the developer has both theoritical and hands-on knowledge." }
            { "role": "user", "content": "A student was asked a question - \"Explain the purpose of @RestController annotation in Spring Boot\". He replied this - \"The @RestController annotation in Spring Boot is used to define RESTful web services. It combines the functionality of @Controller and @ResponseBody annotations into a single convenient annotation. A developer should be able to explain how @RestController differs from @Controller and demonstrate the ability to create RESTful web services using this annotation.\". What should be his score out of 10 for this answer? Provide a structured JSON object containing the score and one-liner justification" }
        ],
        model: "gpt-3.5-turbo",
    });

    console.log(JSON.stringify(completion.choices[0]));
}
main();