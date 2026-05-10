from locust import HttpUser, task, between
import random
from datetime import date, timedelta

class LibraryUser(HttpUser):
    wait_time = between(1, 5)
    
    # Tvoj manuálne vložený token
    token = "eyJraWQiOiJyc2ExIiwidHlwIjoiYXQrand0IiwiYWxnIjoiUlMyNTYifQ.eyJhdWQiOiI3ZTAyYTBhOS00NDZhLTQxMmQtYWQyYi05MGFkZDQ3YjBmZGQiLCJzdWIiOiI1ODgxODBAbXVuaS5jeiIsImFjciI6Imh0dHBzOi8vcmVmZWRzLm9yZy9wcm9maWxlL3NmYSIsInNjb3BlIjoidGVzdF80IHRlc3RfMyB0ZXN0XzIgdGVzdF8xIHRlc3RfcmVhZCBvcGVuaWQgcHJvZmlsZSBlZHVwZXJzb25fc2NvcGVkX2FmZmlsaWF0aW9uIHRlc3Rfd3JpdGUgZW1haWwgdGVzdF81IiwiYXV0aF90aW1lIjoxNzc4NDA0OTk1LCJpc3MiOiJodHRwczovL2lkLm11bmkuY3ovb2lkYy8iLCJleHAiOjE3Nzg0MTQxMDYsImlhdCI6MTc3ODQxMDUwNywiY2xpZW50X2lkIjoiN2UwMmEwYTktNDQ2YS00MTJkLWFkMmItOTBhZGQ0N2IwZmRkIiwianRpIjoiZmRmOWM0ZjUtMmEyOC00YTk0LTkwZjgtOTY3ZWZjMWI5ZDI2In0.UpzDDAo7VRre_eSNXel6Ui0c71QC7-83wgeupzASQdvKysEWJXiwRqzzZLHCD2k8KDFqHPqm6nFPnCS4XJLYeVMZorgKkfVNvIEZfnwOiH_2SeoN3Ip2XW8CWtIR3d2jNTO_0BoIQCjaOKTYutJez056FXcs7eBVDr9EdHQHFhR4n8J4PBvz15oOZDBkKW3UQkQTBaI59f6Jl12fPGKijUm73QrQzaYKYU6UgGbwaxdSmayP2KbWFTKDcHbmMBOE8URw1qsBYtt0VFbWXWaTUKEDqHV5zBOC_SEdkygVDz9A9hb1Kpx9H9V7sxAVsDbPd31b-JUUgtBqXaqc1sjk4w"
    
    def on_start(self):
        # Už nič nerobíme, token máme napevno
        pass
        
    @task(7)
    def search_books(self):
        """User is in the library searching for books."""
        headers = {"Authorization": f"Bearer {self.token}"}
        titles = ["The last wish", "Blood of elves", "Design Patterns", "Java"]
        query = random.choice(titles)
        self.client.get(f"http://localhost:8080/api/books/search?title={query}", headers=headers, name="/api/books/search")
        
    @task(3)
    def view_member_details(self):
        """User is viewing their member details."""
        headers = {"Authorization": f"Bearer {self.token}"}
        member_id = random.randint(1, 10) 
        self.client.get(f"http://localhost:8082/api/members/{member_id}", headers=headers, name="/api/members/{id}")
        
    @task(2)
    def create_lending(self):
        """User is creating a new lending by borrowing a book."""
        headers = {
            "Authorization": f"Bearer {self.token}",
            "Content-Type": "application/json"
        }
        today = date.today().isoformat()
        due = (date.today() + timedelta(days=14)).isoformat()
        
        payload = {
            "bookCopyId": random.randint(1, 3),
            "memberId": random.randint(1, 10),
            "status": "LOANED",
            "loanDate": today,
            "dueDate": due
        }
        self.client.post("http://localhost:8081/api/loans", json=payload, headers=headers, name="/api/loans")


        
    @task(1)
    def check_stats(self):
        """ Simulácia admina, ktorý kontroluje štatistiky """
        headers = {"Authorization": f"Bearer {self.token}"}
        self.client.get("http://localhost:8083/api/stats/popular-books?limit=5", headers=headers, name="/api/stats/popular-books")
        self.client.get("http://localhost:8083/api/notifications", headers=headers, name="/api/notifications")